package one.wangwei.blockchain.network.message.handle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.*;
import one.wangwei.blockchain.util.SerializeUtils;

/**
 * 抽象消息处理器
 *
 * @author wangwei
 * @date 2018/08/26
 */
@Data
@Slf4j
@AllArgsConstructor
public class BaseHandler implements HandlerInterface {

    private Node node;

    /**
     * 消息处理
     *
     * @param peerConn
     * @param peerMessage
     * @param blockchain
     */
    @Override
    public void handleMessage(PeerConnection peerConn, PeerMessage peerMessage, Blockchain blockchain) {
        throw new UnsupportedOperationException("Invalid Message Handler ! ");
    }

    /**
     * 检查
     *
     * @param peerConn
     * @return
     */
    protected boolean checkPeerLimit(PeerConnection peerConn) {
        if (node.maxPeersReached()) {
            log.error("max peers reached " + node.getMaxPeers());
            peerConn.sendData(new PeerMessage(MessageTypEnum.ERROR,
                    new StringMessageData("Join: too many peers")));
            return true;
        }
        return false;
    }

    /**
     * 获取消息数据对象
     *
     * @param peerMessage
     * @return
     */
    @Override
    public BaseMessageData getMsgData(PeerMessage peerMessage) {
        String msgType = peerMessage.getMsgType();
        if (MessageTypEnum.JOIN.type.equals(msgType)) {
            return (JoinMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        if (MessageTypEnum.VERSION.type.equals(msgType)) {
            return (VersionMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        if (MessageTypEnum.GETBLOCKS.type.equals(msgType)) {
            return (GetBlocksMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        throw new RuntimeException("Invalid Message Type ! type = " + msgType);
    }
}


