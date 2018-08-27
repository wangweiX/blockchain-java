package one.wangwei.blockchain.network;

import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.socket.AbstractSocketFactory;
import one.wangwei.blockchain.network.socket.SocketInterface;
import one.wangwei.blockchain.util.LoggerUtil;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Encapsulates a socket connection to a peer, providing simple, reliable
 * send and receive functionality. All data sent to a peer through this
 * class must be formatted as a PeerMessage object.
 *
 * @author Nadeem Abdul Hamid
 */
public class PeerConnection {

    private PeerInfo pd;
    private SocketInterface s;

    /**
     * Opens a new connection to the specified peer.
     *
     * @param info the peer node to connect to
     * @throws IOException          if an I/O error occurs
     * @throws UnknownHostException
     */
    public PeerConnection(PeerInfo info) throws IOException, UnknownHostException {
        pd = info;
        s = AbstractSocketFactory.getSocketFactory().makeSocket(pd.getHost(), pd.getPort());
    }

    /**
     * Constructs a connection for which a socket has already been
     * opened.
     *
     * @param info
     * @param socket
     */
    public PeerConnection(PeerInfo info, SocketInterface socket) {
        pd = info;
        s = socket;
    }


    /**
     * Sends a PeerMessage to the connected peer.
     *
     * @param msg the message object to send
     */
    public void sendData(PeerMessage msg) {
        try {
            s.write(msg.toBytes());
        } catch (IOException e) {
            LoggerUtil.getLogger().warning("Error sending message: " + e);
        }
    }

    /**
     * 从相连的Peer节点接收返回数据
     *
     * @return
     */
    public PeerMessage recvData() {
        try {
            return new PeerMessage(s);
        } catch (IOException e) {
            if (!e.getMessage().equals("EOF in PeerMessage constructor: type"))
                LoggerUtil.getLogger().warning("Error receiving message: " + e);
            else
                LoggerUtil.getLogger().finest("Error receiving message: " + e);
            return null;
        }
    }

    /**
     * 关闭socket连接
     */
    public void close() {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                LoggerUtil.getLogger().warning("Error closing: " + e);
            }
            s = null;
        }
    }

    public PeerInfo getPeerInfo() {
        return pd;
    }

    @Override
    public String toString() {
        return "PeerConnection[" + pd + "]";
    }

}
