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

    public TXInput() {
    }

    public TXInput(byte[] txId, int txOutputIndex, String scriptSig) {
        this();
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.scriptSig = scriptSig;
    }

    /**
     * 判断解锁数据是否能够解锁交易输出
     *
     * @param unlockingData
     * @return
     */
    public boolean canUnlockOutputWith(String unlockingData) {
        return this.getScriptSig().endsWith(unlockingData);
    }

}
