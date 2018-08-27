package one.wangwei.blockchain.network.message.handle;

import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.PeerInfo;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.JoinMessageData;
import one.wangwei.blockchain.network.message.data.StringMessageData;

/**
 * 新节点加入消息处理
 *
 * @author wangwei
 * @date 2018/08/26
 */
@Slf4j
public class JoinHandler extends BaseHandler {

    public JoinHandler(Node node) {
        super(node);
    }

    /**
     * 消息处理
     *
     * @param peerConn
     * @param peerMessage
     */
    @Override
    public void handleMessage(PeerConnection peerConn, PeerMessage peerMessage, Blockchain blockchain) {
        if (checkPeerLimit(peerConn)) {
            return;
        }
        JoinMessageData messageData = (JoinMessageData) this.getMsgData(peerMessage);
        PeerInfo info = messageData.getMyPeerInfo();
        if (this.getNode().getPeer(info.getId()) != null) {
            peerConn.sendData(new PeerMessage(MessageTypEnum.ERROR,
                    new StringMessageData("Join: peer already inserted")));
        } else if (info.getId().equals(this.getNode().getId())) {
            peerConn.sendData(new PeerMessage(MessageTypEnum.ERROR,
                    new StringMessageData("Join: attempt to insert self")));
        } else {
            this.getNode().addPeer(info);
            peerConn.sendData(new PeerMessage(MessageTypEnum.REPLY,
                    new StringMessageData("Join: peer added: " + info.getId())));
        }
    }

}
