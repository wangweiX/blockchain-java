package one.wangwei.blockchain.network.peer.network.message;

import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.Connection;

/**
 * Informs the new peer about this peer
 */
@Slf4j
public class Handshake implements Message {

    private static final long serialVersionUID = 213352944600339280L;

    private final String peerName;

    private final String leaderName;

    public Handshake(String peerName, String leaderName) {
        this.peerName = peerName;
        this.leaderName = leaderName;
    }

    @Override
    public void handle(Peer peer, Connection connection) {
        final String peerName = connection.getPeerName();
        if (peerName == null) {
            connection.setPeerName(this.peerName);
            peer.handleConnectionOpened(connection, leaderName);
        } else if (!peerName.equals(this.peerName)) {
            log.warn("Mismatching peer name received from connection! Existing: "
                    + this.peerName + " Received: " + this.peerName);
        }
    }

}
