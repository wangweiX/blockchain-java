package one.wangwei.blockchain.network.message.handle;

import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.TxMessageData;
import one.wangwei.blockchain.transaction.Transaction;
import org.apache.commons.codec.binary.Hex;

/**
 * 交易处理
 *
 * @author wangwei
 * @date 2018/08/27
 */
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
        this.getMemPool().put(Hex.encodeHexString(tx.getTxId()), tx);

        // TODO
    }
}
