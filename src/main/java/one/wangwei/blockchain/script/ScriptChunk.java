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
     * 如果该组块是非推送数据内容的单个字节（可能是OP_RESERVED或某些无效的操作码）
     *
     * @return
     */
    public boolean isOpCode() {
        return opcode > OP_PUSHDATA4;
    }

    /**
     * 如果该组块是pushdata内容（包括单字节pushdatas），则返回true。
     *
     * @return
     */
    public boolean isPushData() {
        return opcode <= OP_16;
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
