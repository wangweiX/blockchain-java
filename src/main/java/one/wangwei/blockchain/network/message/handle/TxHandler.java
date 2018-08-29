package one.wangwei.blockchain.network.message.handle;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.PeerInfo;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.InventoryMessageData;
import one.wangwei.blockchain.network.message.data.TxMessageData;
import one.wangwei.blockchain.transaction.Transaction;
import one.wangwei.blockchain.transaction.UTXOSet;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 交易处理
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Slf4j
public class TxHandler extends BaseHandler {

    public TxHandler(Node node) {
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
        TxMessageData txMessageData = (TxMessageData) this.getMsgData(peerMessage);
        Transaction tx = txMessageData.getTx();

        // 交易先放入内存池中
        this.getMemPool().put(Hex.encodeHexString(tx.getTxId()), tx);

        // 如果当前节点为中央节点，则将请求转发到其他的节点，中央节点不进行挖矿
        PeerInfo myPeerInfo = this.getNode().getMyInfo();

        // TODO 设置中央节点
        PeerInfo center = new PeerInfo();
        if (myPeerInfo.equals(center)) {
            for (PeerInfo node : this.getNode().getPeers().values()) {
                if (!node.equals(myPeerInfo) && !node.equals(txMessageData.getMyPeerInfo())) {
                    InventoryMessageData inventoryMessageData = new InventoryMessageData();
                    inventoryMessageData.setMyPeerInfo(myPeerInfo);
                    inventoryMessageData.setNTime(System.currentTimeMillis());
                    inventoryMessageData.setInvType(InvTypeEnum.TX);
                    inventoryMessageData.addTxIdHash(tx.getTxId());

                    peerConn.sendData(new PeerMessage(MessageTypEnum.INVENTORY, inventoryMessageData));
                }
            }
        } else {
            // TODO 设置矿工
            String miningAddress = "";
            // 内存池中存在2笔及以上的交易记录时，开始挖矿
            if (getMemPool().size() > 2 && StringUtils.isNotBlank(miningAddress)) {
                minerTx(peerConn, blockchain, myPeerInfo, miningAddress);
            }
        }
    }

    private void minerTx(PeerConnection peerConn, Blockchain blockchain, PeerInfo myPeerInfo, String miningAddress) {
        List<Transaction> verifyTx = Lists.newArrayList();
        for (Transaction memTx : getMemPool().values()) {
            if (blockchain.verifyTransactions(memTx)) {
                verifyTx.add(memTx);
            }
        }

        if (verifyTx.isEmpty()) {
            log.info("All transactions are invalid! Waiting for new ones...");
            return;
        }

        // 挖矿奖励
        verifyTx.add(Transaction.newCoinbaseTX(miningAddress, ""));

        Block mineBlock = blockchain.mineBlock(verifyTx.toArray(new Transaction[verifyTx.size()]));
        UTXOSet utxoSet = new UTXOSet(blockchain);
        utxoSet.update(mineBlock);

        log.info("New block is mined ! ");

        // 删除已经挖矿的交易
        for (Transaction delTx : verifyTx) {
            this.getMemPool().remove(Hex.encodeHexString(delTx.getTxId()));
        }

        for (PeerInfo node : this.getNode().getPeers().values()) {
            if (node.equals(myPeerInfo)) {

                InventoryMessageData inventoryMessageData = new InventoryMessageData();
                inventoryMessageData.addBlockHash(mineBlock.getHash());
                inventoryMessageData.setInvType(InvTypeEnum.BLOCK);
                inventoryMessageData.setMyPeerInfo(myPeerInfo);
                inventoryMessageData.setNTime(System.currentTimeMillis());

                peerConn.sendData(new PeerMessage(MessageTypEnum.BLOCK, inventoryMessageData));
            }
        }

        if (this.getMemPool().size() > 0) {
            this.minerTx(peerConn, blockchain, myPeerInfo, miningAddress);
        }
    }
}
