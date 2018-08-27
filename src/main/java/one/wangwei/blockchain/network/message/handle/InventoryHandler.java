package one.wangwei.blockchain.network.message.handle;

import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.GetDataMessageData;
import one.wangwei.blockchain.network.message.data.InventoryMessageData;

import java.util.List;

/**
 * 存活清单处理
 *
 * @author wangwei
 * @date 2018/08/27
 */
public class InventoryHandler extends BaseHandler {

    public InventoryHandler(Node node) {
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
        if (checkPeerLimit(peerConn)) {
            return;
        }
        InventoryMessageData inventoryMessageData = (InventoryMessageData) this.getMsgData(peerMessage);
        if (InvTypeEnum.BLOCK == inventoryMessageData.getInvType()) {
            List<String> blockHashes = inventoryMessageData.getBlockHashes();

            // 设置待同步的区块Hash列表
            this.setBlocksInTransit(blockHashes);

            GetDataMessageData messageData = new GetDataMessageData();
            messageData.setId(blockHashes.get(0));
            messageData.setType(InvTypeEnum.BLOCK);
            messageData.setMyPeerInfo(this.getNode().getMyInfo());
            messageData.setNTime(System.currentTimeMillis());

            peerConn.sendData(new PeerMessage(MessageTypEnum.GETDATA, messageData));

            // 删除已发送过指令的区块Hash
            this.getBlocksInTransit().remove(blockHashes.get(0));
        }

        if (InvTypeEnum.TX == inventoryMessageData.getInvType()) {
            List<String> txHashes = inventoryMessageData.getTxHashes();
            String txId = txHashes.get(0);
            if (this.getMemPool().get(txId) == null) {

                GetDataMessageData messageData = new GetDataMessageData();
                messageData.setId(txId);
                messageData.setType(InvTypeEnum.TX);
                messageData.setMyPeerInfo(this.getNode().getMyInfo());
                messageData.setNTime(System.currentTimeMillis());

                peerConn.sendData(new PeerMessage(MessageTypEnum.GETDATA, messageData));
            }
        }
    }
}
