package one.wangwei.blockchain.network.peer.network.message.ping;

import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.Connection;
import one.wangwei.blockchain.network.peer.network.message.Message;

public class CancelPings implements Message {

    private static final long serialVersionUID = -8650899535821394626L;

    private String peerName;

    public CancelPings(String peerName) {
        this.peerName = peerName;
    }

    @Override
    public void handle(Peer peer, Connection connection) {
        peer.cancelPings(connection, peerName);
    }

    @Override
    public String toString() {
        return "RemovePings{" +
                "peerName='" + peerName + '\'' +
                '}';
    }

}
