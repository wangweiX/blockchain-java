package one.wangwei.blockchain.script;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkState;
import static one.wangwei.blockchain.script.ScriptOpCodes.OP_PUSHDATA4;

/**
 * 脚本元素基本组块
 *
 * @author wangwei
 * @date 2018/05/07
 */
@Data
@AllArgsConstructor
public class ScriptChunk {

    /**
     * 操作码
     * {@link ScriptOpCodes}
     */
    private int opcode;
    private byte[] data;

    public void write(OutputStream stream) throws IOException {
        if (isOpCode()) {
            checkState(data == null);
            stream.write(opcode);
        }
    }

    /**
     * 判断是否为操作码
     *
     * @return
     */
    public boolean isOpCode() {
        return opcode > OP_PUSHDATA4;
    }

}
