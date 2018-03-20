package one.wangwei.blockchain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.crypto.Base58Check;

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
     * 锁定脚本
     */
    private String scriptPubKey;
    /**
     * 公钥Hash
     */
    private byte[] pubHashKey;


    /**
     * 使用钱包地址锁住交易输出
     *
     * @param address
     */
    public void lock(String address) {
        Base58Check.base58ToBytes(address);


    }

    /**
     * 判断解锁数据是否能够解锁交易输出
     *
     * @param unlockingData
     * @return
     */
    public boolean canBeUnlockedWith(String unlockingData) {
        return this.getScriptPubKey().endsWith(unlockingData);
    }
}
