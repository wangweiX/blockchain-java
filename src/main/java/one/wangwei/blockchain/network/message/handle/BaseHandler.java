package one.wangwei.blockchain.network.message.handle;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.*;
import one.wangwei.blockchain.transaction.Transaction;
import one.wangwei.blockchain.util.SerializeUtils;

import java.util.List;
import java.util.Map;

/**
 * 抽象消息处理器
 *
 * @author wangwei
 * @date 2018/08/26
 */
@Data
@Slf4j
public class BaseHandler implements HandlerInterface {

    private Node node;

    /**
     * 待同步的区块Hash列表
     */
    private List<String> blocksInTransit = Lists.newArrayList();
    /**
     * 交易内存池
     */
    private Map<String, Transaction> memPool;

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
        if (MessageTypEnum.INVENTORY.type.equals(msgType)) {
            return (InventoryMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        if (MessageTypEnum.GETDATA.type.equals(msgType)) {
            return (GetDataMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        if (MessageTypEnum.BLOCK.type.equals(msgType)) {
            return (BlockMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        if (MessageTypEnum.TX.type.equals(msgType)) {
            return (TxMessageData) SerializeUtils.deserialize(peerMessage.getMsgDataBytes());
        }
        throw new RuntimeException("Invalid Message Type ! type = " + msgType);
    }


}


