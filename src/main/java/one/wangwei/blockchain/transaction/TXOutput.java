package one.wangwei.blockchain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.util.Base58Check;

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
     * 创建交易输出
     *
     * @param value
     * @param address
     * @return
     */
    public static TXOutput newTXOutput(int value, String address) {
        // 反向转化为 byte 数组
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
        return new TXOutput(value, pubKeyHash);
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
