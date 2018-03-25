package one.wangwei.blockchain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.util.BtcAddressUtils;

import java.util.Arrays;

/**
 * 交易输入
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TXInput {

    /**
     * 交易Id的hash值
     */
    private byte[] txId;
    /**
     * 交易输出索引
     */
    private int txOutputIndex;
    /**
     * 签名
     */
    private byte[] signature;
    /**
     * 公钥
     */
    private byte[] pubKey;


    /**
     * 检查公钥hash是否用于交易输入
     *
     * @param pubKeyHash
     * @return
     */
    public boolean usesKey(byte[] pubKeyHash) {
        byte[] lockingHash = BtcAddressUtils.ripeMD160Hash(this.getPubKey());
        return Arrays.equals(lockingHash, pubKeyHash);
    }

}
