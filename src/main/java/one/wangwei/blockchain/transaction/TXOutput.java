package one.wangwei.blockchain.transaction;

import lombok.Data;

/**
 * 交易输出
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
public class TXOutput {

    /**
     * 数值
     */
    private int value;
    /**
     * 锁定脚本
     */
    private String scriptPubKey;

    public TXOutput() {
    }

    public TXOutput(int value, String scriptPubKey) {
        this();
        this.value = value;
        this.scriptPubKey = scriptPubKey;
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
