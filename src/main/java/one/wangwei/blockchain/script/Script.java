package one.wangwei.blockchain.script;

import com.google.common.collect.Lists;
import lombok.Data;

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
     * 将 0 ~ 16 之间的数字转化为操作码
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
}
