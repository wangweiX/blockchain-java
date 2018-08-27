package one.wangwei.blockchain.network.message.handle;

import lombok.Data;
import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.BlockMessageData;
import one.wangwei.blockchain.network.message.data.GetDataMessageData;
import one.wangwei.blockchain.transaction.UTXOSet;

@Data
public class BlockHandler extends BaseHandler {

    public BlockHandler(Node node) {
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
        BlockMessageData blockMessageData = (BlockMessageData) this.getMsgData(peerMessage);

        Block block = blockMessageData.getBlock();
        blockchain.saveBlock(block);

        if (this.getBlocksInTransit().size() > 0) {
            String blockHash = this.getBlocksInTransit().get(0);
            GetDataMessageData messageData = new GetDataMessageData();
            messageData.setType(InvTypeEnum.BLOCK);
            messageData.setId(blockHash);
            peerConn.sendData(new PeerMessage(MessageTypEnum.GETDATA, messageData));

            // 删除已发送过指令的区块Hash
            this.getBlocksInTransit().remove(0);

        } else {
            UTXOSet utxoSet = new UTXOSet(blockchain);
            utxoSet.reIndex();
        }

    }
}
