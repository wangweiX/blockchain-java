package one.wangwei.blockchain.network.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.transaction.Transaction;

/**
 * 交易消息
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxMessageData extends BaseMessageData {

    private Transaction tx;

}
