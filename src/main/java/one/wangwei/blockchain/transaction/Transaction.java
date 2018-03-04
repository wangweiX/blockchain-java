package one.wangwei.blockchain.transaction;

import lombok.Data;

/**
 * 交易
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
public class Transaction {

    /**
     * 交易的Hash
     */
    private byte[] txId;
    /**
     * 交易输入
     */
    private TXInput[] inputs;
    /**
     * 交易输出
     */
    private TXOutput[] outputs;

}
