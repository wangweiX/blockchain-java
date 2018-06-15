package one.wangwei.blockchain.script;

import lombok.Data;
import one.wangwei.blockchain.util.BtcAddressUtils;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static one.wangwei.blockchain.script.ScriptOpCodes.*;

/**
 * 脚本构建器
 *
 * @author wangwei
 * @date 2018/05/07
 */
@Data
public class ScriptBuilder {

    /**
     * 脚本组块
     */
    private List<ScriptChunk> chunks;

    /**
     * 在index处添加脚本组块
     *
     * @param index
     * @param chunk
     * @return
     */
    public ScriptBuilder addChunk(int index, ScriptChunk chunk) {
        chunks.add(index, chunk);
        return this;
    }

    /**
     * 脚本末尾处添加脚本组块
     *
     * @param chunk
     * @return
     */
    public ScriptBuilder addChunk(ScriptChunk chunk) {
        return addChunk(chunks.size(), chunk);
    }

    /**
     * 添加操作码
     *
     * @param opcode
     * @return
     */
    public ScriptBuilder op(int opcode) {
        return op(chunks.size(), opcode);
    }

    /**
     * 添加操作码
     *
     * @param index
     * @param opcode
     * @return
     */
    public ScriptBuilder op(int index, int opcode) {
        checkArgument(opcode > OP_PUSHDATA4);
        return addChunk(index, new ScriptChunk(opcode, null));
    }

    /**
     * 添加数据
     *
     * @param data
     * @return
     */
    public ScriptBuilder data(byte[] data) {
        if (data.length == 0) {
            return smallNum(0);
        }
        return data(chunks.size(), data);
    }

    /**
     * 添加数据
     *
     * @param index 索引位置
     * @param data  数据
     * @return
     */
    public ScriptBuilder data(int index, byte[] data) {
        byte[] copy = Arrays.copyOf(data, data.length);
        int opcode = getOpcodeFromData(data);
        return addChunk(index, new ScriptChunk(opcode, copy));
    }

    /**
     * 获取 data 数据对应的操作码
     *
     * @param data
     * @return
     */
    private int getOpcodeFromData(byte[] data) {
        int opcode;
        if (data.length == 0) {
            opcode = OP_0;
        } else if (data.length == 1) {
            byte b = data[0];
            if (b >= 1 && b <= 16) {
                opcode = Script.encodeToOpN(b);
            } else {
                opcode = 1;
            }
        } else if (data.length < OP_PUSHDATA1) {
            opcode = data.length;
        } else if (data.length < 256) {
            opcode = OP_PUSHDATA1;
        } else if (data.length < 65536) {
            opcode = OP_PUSHDATA2;
        } else {
            throw new RuntimeException("Unimplemented");
        }
        return opcode;
    }

    /**
     * 将0-16之间的数字作为OP_N操作码添加到程序的末尾。
     *
     * @param num
     * @return
     */
    public ScriptBuilder smallNum(int num) {
        return smallNum(chunks.size(), num);
    }

    /**
     * 将0-16之间的数字作为OP_N操作码添加到程序的末尾。
     *
     * @param index
     * @param num
     * @return
     */
    public ScriptBuilder smallNum(int index, int num) {
        checkArgument(num >= 0, "Cannot encode negative numbers with smallNum");
        checkArgument(num <= 16, "Cannot encode numbers larger than 16 with smallNum");
        return addChunk(index, new ScriptChunk(Script.encodeToOpN(num), null));
    }

    public Script build() {
        return new Script(this.chunks);
    }

    /**
     * 创建锁定脚本(P2PKH)
     *
     * @return
     */
    public static Script createOutputScript(String address) {
        return createOutputScript(BtcAddressUtils.getRipeMD160Hash(address));
    }

    /**
     * 创建锁定脚本(P2PKH)
     *
     * @return
     */
    public static Script createOutputScript(byte[] pubkHash) {
        return new ScriptBuilder()
                .op(OP_DUP)
                .op(OP_HASH160)
                .data(pubkHash)
                .op(OP_EQUALVERIFY)
                .op(OP_CHECKSIG)
                .build();
    }

    /**
     * 创建解锁脚本
     *
     * @return
     */
    public static Script createInputScript(byte[] sigBytes, byte[] pubKeyBytes) {
        return new ScriptBuilder().data(sigBytes).data(pubKeyBytes).build();
    }

}

