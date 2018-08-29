package one.wangwei.blockchain.network.message.handle;

import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.BaseMessageData;

/**
 * 消息处理接口
 *
 * @author wangwei
 * @date 2018/08/26
 */
public interface HandlerInterface<T extends BaseMessageData> {

    /**
     * 消息处理
     *
     * @param peerConn
     * @param peerMessage
     * @param blockchain
     */
    public void handleMessage(PeerConnection peerConn, PeerMessage peerMessage, Blockchain blockchain);

    /**
     * 获取消息数据对象
     *
     * @param peerMessage
     * @return
     */
    public T getMsgData(PeerMessage peerMessage);

}