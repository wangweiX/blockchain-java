package one.wangwei.blockchain.network.peer.network.message;

import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.Connection;

import java.io.Serializable;

/**
 * Interfaces of the messages dispatched between peers in the network
 */
public interface Message extends Serializable {

    void handle(Peer peer, Connection connection);

}
