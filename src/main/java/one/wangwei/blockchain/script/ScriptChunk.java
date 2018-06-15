package one.wangwei.blockchain.script;

import com.google.common.io.BaseEncoding;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkState;
import static one.wangwei.blockchain.script.ScriptOpCodes.*;

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

    public boolean equalsOpCode(int opcode) {
        return opcode == this.opcode;
    }

    /**
     * 判断是否为操作码
     *
     * @return
     */
    public boolean isOpCode() {
        return opcode > OP_PUSHDATA4;
    }

    public void write(OutputStream stream) throws IOException {
        if (isOpCode()) {
            checkState(data == null);
            stream.write(opcode);
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (isOpCode()) {
            buf.append(getOpCodeName(opcode));
        } else if (data != null) {
            // Data chunk
            buf.append(getPushDataName(opcode))
                    .append("[")
                    .append(BaseEncoding.base16().lowerCase().encode(data))
                    .append("]");
        } else {
            // Small num
            buf.append(Script.decodeFromOpN(opcode));
        }
        return buf.toString();
    }

}
