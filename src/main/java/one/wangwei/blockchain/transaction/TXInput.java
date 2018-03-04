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
     *
     */
    private int vOut;
    /**
     * 解锁脚本
     */
    private String scriptSig;

}
