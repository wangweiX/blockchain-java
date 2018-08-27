package one.wangwei.blockchain.network.message.handle;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.BlockMessageData;
import one.wangwei.blockchain.network.message.data.GetDataMessageData;
import one.wangwei.blockchain.transaction.Transaction;

/**
 * 获取数据
 *
 * @author wangwei
 * @date 2018/08/27
 */
public class GetDataHander extends BaseHandler {

    public GetDataHander(Node node) {
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
        GetDataMessageData getDataMessageData = (GetDataMessageData) this.getMsgData(peerMessage);

        if (getDataMessageData.getType() == InvTypeEnum.BLOCK) {
            Block block = blockchain.getBlockByHash(getDataMessageData.getId());
            BlockMessageData blockMessageData = new BlockMessageData();
            blockMessageData.setBlock(block);
            peerConn.sendData(new PeerMessage(MessageTypEnum.BLOCK, blockMessageData));
        }

        if (getDataMessageData.getType() == InvTypeEnum.TX) {
            String txId = getDataMessageData.getId();
            Transaction tx = this.getMemPool().get(txId);

            // TODO
        }
    }
}
