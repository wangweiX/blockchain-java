package one.wangwei.blockchain.network.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.block.Block;

/**
 * 区块
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockMessageData extends BaseMessageData {

    private Block block;

}
