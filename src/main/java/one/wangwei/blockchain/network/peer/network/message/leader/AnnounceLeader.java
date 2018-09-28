package one.wangwei.blockchain.network.peer.network.message.leader;

import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.Connection;
import one.wangwei.blockchain.network.peer.network.message.Message;

/**
 * Notifies neighbours about announced leader
 */
public class AnnounceLeader implements Message {

    private static final long serialVersionUID = 81362517392480723L;

    private final String leaderName;

    public AnnounceLeader(String leaderName) {
        this.leaderName = leaderName;
    }

    @Override
    public void handle(Peer peer, Connection connection) {
        peer.handleLeader(connection, leaderName);
    }

}
