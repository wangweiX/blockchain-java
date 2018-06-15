package one.wangwei.blockchain.script;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;
import one.wangwei.blockchain.util.BtcAddressUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
            for (ScriptChunk chunk : chunks) {
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

}
