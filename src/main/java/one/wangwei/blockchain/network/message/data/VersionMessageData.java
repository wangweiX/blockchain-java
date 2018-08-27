package one.wangwei.blockchain.network.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 版本消息
 *
 * @author wangwei
 * @date 2018/08/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionMessageData extends BaseMessageData {

    /**
     * 版本号
     */
    private int clientVersion = 0;
    /**
     * 当前节点区块链的高度
     */
    private long bestHeight;

}
