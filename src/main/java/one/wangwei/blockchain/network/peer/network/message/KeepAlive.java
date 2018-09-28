package one.wangwei.blockchain.network.peer.network.message;

import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.Connection;

/**
 * Sent to neighbours to notify them about presence of this peer
 */
@Slf4j
public class KeepAlive implements Message {

    private static final long serialVersionUID = -4998803925489492616L;

    @Override
    public void handle(Peer peer, Connection connection) {
        log.debug("Keep alive ping received from {}", connection);
    }

}
