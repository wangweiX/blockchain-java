package one.wangwei.blockchain.network.message.handle;

import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.InventoryMessageData;

/**
 * 获取区块消息处理器
 *
 * @author wangwei
 * @date 2018/08/27
 */
public class GetBlocksHandler extends BaseHandler {

    public GetBlocksHandler(Node node) {
        super(node);
    }

    /**
     * 消息处理
     *
     * @param peerConn
     * @param peerMessage
     * @param blockchain
     */
    @Override
    public void handleMessage(PeerConnection peerConn, PeerMessage peerMessage, Blockchain blockchain) {
        InventoryMessageData messageData = new InventoryMessageData();
        messageData.setInvType(InvTypeEnum.BLOCK);
        messageData.setBlockHashes(blockchain.getAllBlockHash());
        messageData.setNTime(System.currentTimeMillis());
        messageData.setMyPeerInfo(this.getNode().getMyInfo());

        peerConn.sendData(new PeerMessage(MessageTypEnum.INVENTORY, messageData));
    }
}
