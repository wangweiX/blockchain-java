package one.wangwei.blockchain.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.MessageData;
import one.wangwei.blockchain.transaction.Transaction;
import one.wangwei.blockchain.transaction.UTXOSet;
import one.wangwei.blockchain.util.SerializeUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p> 区块链网络节点 </p>
 *
 * @author wangwei
 * @date 2018/08/24
 */
@Slf4j
public class BlockchainNode extends Node {

    public BlockchainNode(int maxPeers, PeerInfo myInfo) {
        super(maxPeers, myInfo);
        this.addRouter(new Router(this));
        this.addHandler(MessageTypEnum.JOIN.type, new JoinHandler(this));
        this.addHandler(MessageTypEnum.VERSION.type, new VersionHandler(this));
        this.addHandler(MessageTypEnum.GETBLOCKS.type, new GetBlocksHandler(this));
        this.addHandler(MessageTypEnum.TX.type, new TxHandler(this));
        this.addHandler(MessageTypEnum.INVENTORY.type, new InventoryHandler(this));
        this.addHandler(MessageTypEnum.GETDATA.type, new GetDataHandler(this));
        this.addHandler(MessageTypEnum.BLOCK.type, new BlockHandler(this));
    }

    private class Router implements RouterInterface {
        private Node peer;

        public Router(Node peer) {
            this.peer = peer;
        }

        @Override
        public PeerInfo route(String peerId) {
            if (peer.getPeerKeys().contains(peerId)) {
                return peer.getPeer(peerId);
            } else {
                return null;
            }
        }
    }

    /**
     * 消息处理基础类
     */
    @Data
    private class BaseHandler implements HandlerInterface {

        private Node node;

        /**
         * 待同步的区块Hash列表
         */
        private List<String> blocksInTransit = Lists.newCopyOnWriteArrayList();
        /**
         * 交易内存池
         */
        private Map<String, Transaction> memPool = Maps.newConcurrentMap();

        public BaseHandler(Node node) {
            this.node = node;
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
                        new MessageData.StringMessageData("Join: too many peers")));
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
        public MessageData getMsgData(PeerMessage peerMessage) {
            String msgType = peerMessage.getMsgType();
            if (MessageTypEnum.JOIN.type.equals(msgType)) {
                return (MessageData.JoinMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            if (MessageTypEnum.VERSION.type.equals(msgType)) {
                return (MessageData.VersionMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            if (MessageTypEnum.GETBLOCKS.type.equals(msgType)) {
                return (MessageData.GetBlocksMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            if (MessageTypEnum.INVENTORY.type.equals(msgType)) {
                return (MessageData.InventoryMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            if (MessageTypEnum.GETDATA.type.equals(msgType)) {
                return (MessageData.GetDataMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            if (MessageTypEnum.BLOCK.type.equals(msgType)) {
                return (MessageData.BlockMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            if (MessageTypEnum.TX.type.equals(msgType)) {
                return (MessageData.TxMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
            }
            throw new RuntimeException("Invalid Message Type ! type = " + msgType);
        }
    }

    /**
     * 新节点加入消息处理
     */
    private class JoinHandler extends BlockchainNode.BaseHandler {
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
            MessageData.JoinMessageData messageData = (MessageData.JoinMessageData) this.getMsgData(peerMessage);
            PeerInfo info = messageData.getMyPeerInfo();
            if (this.getNode().getPeer(info.getId()) != null) {
                peerConn.sendData(new PeerMessage(MessageTypEnum.ERROR,
                        new MessageData.StringMessageData("Join: peer already inserted")));
            } else if (info.getId().equals(this.getNode().getId())) {
                peerConn.sendData(new PeerMessage(MessageTypEnum.ERROR,
                        new MessageData.StringMessageData("Join: attempt to insert self")));
            } else {
                this.getNode().addPeer(info);
                peerConn.sendData(new PeerMessage(MessageTypEnum.REPLY,
                        new MessageData.StringMessageData("Join: peer added: " + info.getId())));
            }
        }

    }

    /**
     * 版本消息处理
     */
    private class VersionHandler extends BlockchainNode.BaseHandler {

        public VersionHandler(Node node) {
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

            MessageData.VersionMessageData messageData = (MessageData.VersionMessageData) this.getMsgData(peerMessage);

            long foreignerBestHeight = messageData.getBestHeight();
            long myBestHeight = blockchain.getBestHeight();

            // 如果本地区块高度小于其他节点，则从对方节点下载区块数据
            if (myBestHeight < foreignerBestHeight) {
                MessageData.GetBlocksMessageData getBlocksMessageData = new MessageData.GetBlocksMessageData();
                getBlocksMessageData.setMyPeerInfo(this.getNode().getMyInfo());
                getBlocksMessageData.setNTime(System.currentTimeMillis());
                peerConn.sendData(new PeerMessage(MessageTypEnum.GETBLOCKS, getBlocksMessageData));
            }
            // 如果本地区块高度大于其他节点，则发送version指令，让对方下载区块数据
            else if (myBestHeight > foreignerBestHeight) {
                MessageData.VersionMessageData myVersionData = new MessageData.VersionMessageData();
                myVersionData.setBestHeight(myBestHeight);
                myVersionData.setMyPeerInfo(this.getNode().getMyInfo());
                myVersionData.setNTime(System.currentTimeMillis());
                peerConn.sendData(new PeerMessage(MessageTypEnum.VERSION, myVersionData));
            }
        }

    }

    /**
     * 获取区块消息处理器
     */
    private class GetBlocksHandler extends BlockchainNode.BaseHandler {

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
            MessageData.InventoryMessageData messageData = new MessageData.InventoryMessageData();
            messageData.setInvType(InvTypeEnum.BLOCK);
            messageData.setBlockHashes(blockchain.getAllBlockHash());
            messageData.setNTime(System.currentTimeMillis());
            messageData.setMyPeerInfo(this.getNode().getMyInfo());

            peerConn.sendData(new PeerMessage(MessageTypEnum.INVENTORY, messageData));
        }
    }

    /**
     * 库存清单处理
     */
    private class InventoryHandler extends BlockchainNode.BaseHandler {

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

            MessageData.InventoryMessageData inventoryMessageData = (MessageData.InventoryMessageData) this.getMsgData(peerMessage);
            if (InvTypeEnum.BLOCK == inventoryMessageData.getInvType()) {
                List<String> blockHashes = inventoryMessageData.getBlockHashes();

                // 设置待同步的区块Hash列表
                this.setBlocksInTransit(blockHashes);

                MessageData.GetDataMessageData messageData = new MessageData.GetDataMessageData();
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

                    MessageData.GetDataMessageData messageData = new MessageData.GetDataMessageData();
                    messageData.setId(txId);
                    messageData.setType(InvTypeEnum.TX);
                    messageData.setMyPeerInfo(this.getNode().getMyInfo());
                    messageData.setNTime(System.currentTimeMillis());

                    peerConn.sendData(new PeerMessage(MessageTypEnum.GETDATA, messageData));
                }
            }
        }
    }

    /**
     * 获取数据
     */
    private class GetDataHandler extends BlockchainNode.BaseHandler {

        public GetDataHandler(Node node) {
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
            MessageData.GetDataMessageData getDataMessageData = (MessageData.GetDataMessageData) this.getMsgData(peerMessage);

            // 处理同步区块请求
            if (getDataMessageData.getType() == InvTypeEnum.BLOCK) {
                Block block = blockchain.getBlockByHash(getDataMessageData.getId());

                MessageData.BlockMessageData blockMessageData = new MessageData.BlockMessageData();
                blockMessageData.setBlock(block);

                peerConn.sendData(new PeerMessage(MessageTypEnum.BLOCK, blockMessageData));
            }

            // 处理同步交易请求
            if (getDataMessageData.getType() == InvTypeEnum.TX) {
                String txId = getDataMessageData.getId();
                Transaction tx = this.getMemPool().get(txId);

                MessageData.TxMessageData txMessageData = new MessageData.TxMessageData();
                txMessageData.setTx(tx);
                txMessageData.setMyPeerInfo(this.getNode().getMyInfo());
                txMessageData.setNTime(System.currentTimeMillis());

                peerConn.sendData(new PeerMessage(MessageTypEnum.TX, txMessageData));
            }
        }
    }

    /**
     * 同步区块消息处理
     */
    private class BlockHandler extends BlockchainNode.BaseHandler {
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
            MessageData.BlockMessageData blockMessageData = (MessageData.BlockMessageData) this.getMsgData(peerMessage);

            Block block = blockMessageData.getBlock();

            // TODO 验证区块

            blockchain.saveBlock(block);

            // 如果还有待同步的区块，则再次发送 GETDATA 指令，向另外的节点请求数据
            if (this.getBlocksInTransit().size() > 0) {
                String blockHash = this.getBlocksInTransit().get(0);
                MessageData.GetDataMessageData messageData = new MessageData.GetDataMessageData();
                messageData.setType(InvTypeEnum.BLOCK);
                messageData.setId(blockHash);
                peerConn.sendData(new PeerMessage(MessageTypEnum.GETDATA, messageData));

                // 删除已发送过指令的区块Hash
                this.getBlocksInTransit().remove(0);

            } else {
                UTXOSet utxoSet = new UTXOSet(blockchain);
                utxoSet.update(block);
            }
        }
    }

    /**
     * 同步交易数据处理
     */
    private class TxHandler extends BlockchainNode.BaseHandler {

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
            MessageData.TxMessageData txMessageData = (MessageData.TxMessageData) this.getMsgData(peerMessage);
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
                        MessageData.InventoryMessageData inventoryMessageData = new MessageData.InventoryMessageData();
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

                    MessageData.InventoryMessageData inventoryMessageData = new MessageData.InventoryMessageData();
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
}

