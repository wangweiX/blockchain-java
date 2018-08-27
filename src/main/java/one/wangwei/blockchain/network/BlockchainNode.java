package one.wangwei.blockchain.network;

import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.handle.GetBlocksHandler;
import one.wangwei.blockchain.network.message.handle.JoinHandler;
import one.wangwei.blockchain.network.message.handle.VersionHandler;

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

}
