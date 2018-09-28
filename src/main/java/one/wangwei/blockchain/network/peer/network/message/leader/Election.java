package one.wangwei.blockchain.network.peer.network.message.leader;

import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.Connection;
import one.wangwei.blockchain.network.peer.network.message.Message;

/**
 * Notifies other peers about the election started by this peer
 */
public class Election implements Message {

    private static final long serialVersionUID = 3025595002500496571L;

    @Override
    public void handle(Peer peer, Connection connection) {
        peer.handleElection(connection);
    }

}
