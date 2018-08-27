package one.wangwei.blockchain.network.message.handle;

import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.GetBlocksMessageData;
import one.wangwei.blockchain.network.message.data.VersionMessageData;

/**
 * 版本消息处理
 *
 * @author wangwei
 * @date 2018/08/26
 */
@Slf4j
public class VersionHandler extends BaseHandler {

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

        VersionMessageData messageData = (VersionMessageData) this.getMsgData(peerMessage);

        long foreignerBestHeight = messageData.getBestHeight();
        long myBestHeight = blockchain.getBestHeight();

        // 如果本地区块高度小于其他节点，则从对方节点下载区块数据
        if (myBestHeight < foreignerBestHeight) {
            GetBlocksMessageData getBlocksMessageData = new GetBlocksMessageData();
            getBlocksMessageData.setMyPeerInfo(this.getNode().getMyInfo());
            getBlocksMessageData.setNTime(System.currentTimeMillis());
            peerConn.sendData(new PeerMessage(MessageTypEnum.GETBLOCKS, getBlocksMessageData));
        }
        // 如果本地区块高度大于其他节点，则发送version指令，让对方下载区块数据
        else if (myBestHeight > foreignerBestHeight) {
            VersionMessageData myVersionData = new VersionMessageData();
            myVersionData.setBestHeight(myBestHeight);
            myVersionData.setMyPeerInfo(this.getNode().getMyInfo());
            myVersionData.setNTime(System.currentTimeMillis());
            peerConn.sendData(new PeerMessage(MessageTypEnum.VERSION, myVersionData));
        }
    }

}
