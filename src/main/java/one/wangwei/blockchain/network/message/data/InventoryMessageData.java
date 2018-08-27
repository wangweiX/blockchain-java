package one.wangwei.blockchain.network.message.data;

import lombok.Data;
import one.wangwei.blockchain.network.message.InvTypeEnum;

import java.util.List;

/**
 * 库存清单消息
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Data
public class InventoryMessageData extends BaseMessageData {

    /**
     * 库存清单类型("tx","block")
     */
    private InvTypeEnum invType;
    /**
     * 区块Hash列表
     */
    private List<String> blockHashes;
    /**
     * 交易Hash列表
     */
    private List<String> txHashes;
}
