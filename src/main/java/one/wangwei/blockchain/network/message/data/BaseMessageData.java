package one.wangwei.blockchain.network.message.data;

import lombok.Data;
import lombok.ToString;
import one.wangwei.blockchain.network.PeerInfo;
import one.wangwei.blockchain.util.SerializeUtils;

/**
 * 消息数据抽象父类
 *
 * @author wangwei
 * @date 2018/08/24
 */
@ToString
@Data
public class BaseMessageData {

    /**
     * 本地地址信息
     */
    protected PeerInfo myPeerInfo;
    /**
     * 当前时间
     */
    protected long nTime;

    /**
     * 消息数据转化为字节数组
     *
     * @return
     */
    public byte[] toBytes() {
        return SerializeUtils.serialize(this);
    }
}
