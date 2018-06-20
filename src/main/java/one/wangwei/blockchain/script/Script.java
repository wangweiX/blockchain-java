package one.wangwei.blockchain.script;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;
import one.wangwei.blockchain.exception.ScriptException;
import one.wangwei.blockchain.transaction.Transaction;
import one.wangwei.blockchain.util.BtcAddressUtils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static one.wangwei.blockchain.script.ScriptOpCodes.*;

/**
 * 脚本
 *
 * @author wangwei
 * @date 2018/06/14
 */
@Data
public class Script {

    /**
     * 脚本最大长度 bytes
     */
    public static final long MAX_SCRIPT_ELEMENT_SIZE = 520;

    private List<ScriptChunk> chunks;
    private byte[] program;

    public Script() {
        chunks = Lists.newArrayList();
    }

    public Script(List<ScriptChunk> chunks) {
        this.chunks = Collections.unmodifiableList(chunks);
    }

    /**
     * 将 0-16 之间的数字转化为操作码
     *
     * @param value
     * @return
     */
    public static int encodeToOpN(int value) {
        checkArgument(value >= -1 && value <= 16, "encodeToOpN called for " + value + " which we cannot encode in an opcode.");
        if (value == 0) {
            return OP_0;
        } else if (value == -1) {
            return OP_1NEGATE;
        } else {
            return value - 1 + OP_1;
        }
    }

    /**
     * 将操作码转化为数字
     *
     * @param opcode
     * @return
     */
    public static int decodeFromOpN(int opcode) {
        checkArgument((opcode == OP_0 || opcode == OP_1NEGATE) || (opcode >= OP_1 && opcode <= OP_16), "decodeFromOpN called on non OP_N opcode");
        if (opcode == OP_0) {
            return 0;
        } else if (opcode == OP_1NEGATE) {
            return -1;
        } else {
            return opcode + 1 - OP_1;
        }
    }

    /**
     * 判断脚本是否为 P2PKH
     *
     * @return
     */
    public boolean isSendToAddress() {
        return chunks.size() == 5
                && chunks.get(0).equalsOpCode(OP_DUP)
                && chunks.get(1).equalsOpCode(OP_HASH160)
                && chunks.get(2).getData().length == BtcAddressUtils.LENGTH
                && chunks.get(3).equalsOpCode(OP_EQUALVERIFY)
                && chunks.get(4).equalsOpCode(OP_CHECKSIG);
    }

    /**
     * 获取脚本的序列化数据
     *
     * @return
     */
    public byte[] getProgram() {
        try {
            if (program != null) {
                return Arrays.copyOf(program, program.length);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (ScriptChunk chunk: chunks) {
                chunk.write(bos);
            }
            program = bos.toByteArray();
            return program;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将脚本转化为字符串，例如:
     * "OP_DUP OP_HASH160 7f9b1a7fb68d60c536c2fd8aeaa53a8f3cc025a8 OP_EQUALVERIFY OP_CHECKSIG"
     *
     * @return
     */
    @Override
    public String toString() {
        return Joiner.on(" ").join(chunks);
    }

    /**
     * 执行脚本
     */
    public static void executeScript(Script script, LinkedList<byte[]> stack, Transaction tx) {
        for (ScriptChunk chunk: script.chunks) {
            int opcode = chunk.getOpcode();
            if (opcode == OP_0) {
                stack.add(new byte[0]);
            } else if (!chunk.isOpCode()) {
                stack.add(chunk.getData());
            } else {
                if (opcode == OP_VERIF || opcode == OP_VERNOTIF) {
                    throw new ScriptException("Script included OP_VERIF or OP_VERNOTIF");
                }
                if (opcode == OP_CAT || opcode == OP_SUBSTR || opcode == OP_LEFT || opcode == OP_RIGHT ||
                        opcode == OP_INVERT || opcode == OP_AND || opcode == OP_OR || opcode == OP_XOR ||
                        opcode == OP_2MUL || opcode == OP_2DIV || opcode == OP_MUL || opcode == OP_DIV ||
                        opcode == OP_MOD || opcode == OP_LSHIFT || opcode == OP_RSHIFT) {
                    throw new ScriptException("Script included a disabled Script Op.");
                }
                switch (opcode) {
                    case OP_DUP:
                        if (stack.size() < 1) {
                            throw new ScriptException("Attempted OP_DUP on an empty stack");
                        }
                        stack.add(stack.getLast());
                        break;
                    case OP_HASH160:
                        if (stack.size() < 1) {
                            throw new ScriptException("Attempted OP_HASH160 on an empty stack");
                        }
                        stack.add(BtcAddressUtils.ripeMD160Hash(stack.pollLast()));
                        break;
                    case OP_EQUALVERIFY:
                        if (stack.size() < 2) {
                            throw new ScriptException("Attempted OP_EQUALVERIFY on a stack with size < 2");
                        }
                        if (!Arrays.equals(stack.pollLast(), stack.pollLast())) {
                            throw new ScriptException("OP_EQUALVERIFY: non-equal data");
                        }
                        break;
                    case OP_CHECKSIG:
                        checkSig(script, stack, tx);
                    default:
                        throw new ScriptException("OP_EQUALVERIFY: non-equal data");
                }
            }
        }
    }

    /**
     * 执行校验码验证
     *
     * @param script
     * @param stack
     * @param tx
     */
    private static void checkSig(Script script, LinkedList<byte[]> stack, Transaction tx) {
        if (stack.size() < 2) {
            throw new ScriptException("Attempted OP_CHECKSIG(VERIFY) on a stack with size < 2");
        }

        byte[] pubKey = stack.pollLast();
        byte[] sigBytes = stack.pollLast();

        try {
            Security.addProvider(new BouncyCastleProvider());
            ECParameterSpec ecParameters = ECNamedCurveTable.getParameterSpec("secp256k1");
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);
            BigInteger x = new BigInteger(1, Arrays.copyOfRange(pubKey, 1, 33));
            BigInteger y = new BigInteger(1, Arrays.copyOfRange(pubKey, 33, 65));
            ECPoint ecPoint = ecParameters.getCurve().createPoint(x, y);

            ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecParameters);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(tx.getTxId());
            if (!ecdsaVerify.verify(sigBytes)) {
                throw new ScriptException("OP_EQUALVERIFY: non-equal data");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }
}

