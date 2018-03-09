package one.wangwei.blockchain.transaction;

import lombok.Data;

import java.util.Map;

/**
 * 查询结果
 *
 * @author wangwei
 * @date 2018/03/09
 */
@Data
public class SpendableOutputResult {

    /**
     * 交易时的支付金额
     */
    private int accumulated;
    /**
     * 未花费的交易
     */
    private Map<String, int[]> unspentOuts;

    public SpendableOutputResult(int accumulated, Map<String, int[]> unspentOuts) {
        this.accumulated = accumulated;
        this.unspentOuts = unspentOuts;
    }
}
