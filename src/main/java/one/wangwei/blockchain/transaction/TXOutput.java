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

}
