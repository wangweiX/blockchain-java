package one.wangwei.blockchain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.script.Script;
import one.wangwei.blockchain.script.ScriptBuilder;
import one.wangwei.blockchain.util.BtcAddressUtils;

import java.util.Arrays;

/**
 * 交易输出
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TXOutput {

    /**
     * 数值
     */
    private int value;
    /**
     * 公钥Hash
     */
    private byte[] pubKeyHash;
    /**
     * p2pkh脚本
     */
    private Script p2pkhScript;

    public TXOutput(int value, byte[] pubKeyHash) {
        this.value = value;
        this.pubKeyHash = pubKeyHash;
        this.p2pkhScript = ScriptBuilder.createOutputScript(pubKeyHash);
    }

    /**
     * 创建交易输出
     *
     * @param value
     * @param address
     * @return
     */
    public static TXOutput newTXOutput(int value, String address) {
        byte[] pubKeyHash = BtcAddressUtils.getRipeMD160Hash(address);
        Script p2pkhScript = ScriptBuilder.createOutputScript(pubKeyHash);
        return new TXOutput(value, pubKeyHash, p2pkhScript);
    }

    /**
     * 检查交易输出是否能够使用指定的公钥
     *
     * @param pubKeyHash
     * @return
     */
    public boolean isLockedWithKey(byte[] pubKeyHash) {
        return Arrays.equals(this.getPubKeyHash(), pubKeyHash);
    }

}
