package one.wangwei.blockchain.transaction;

import lombok.Data;

/**
 * 交易输入
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
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
     * 解锁脚本
     */
    private String scriptSig;

    public TXInput(byte[] txId, int txOutputIndex, String scriptSig) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.scriptSig = scriptSig;
    }
}
