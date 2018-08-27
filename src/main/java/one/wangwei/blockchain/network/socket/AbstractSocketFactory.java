package one.wangwei.blockchain.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Factory for socket implementations. Used by classes of the PeerBase
 * system to generate sockets appropriate for normal use, testing, or
 * educational purposes.
 *
 * @author Nadeem Abdul Hamid
 */

/**
 *
 * @author wangwei
 * @date 2018/08/26
 */
public abstract class AbstractSocketFactory {

    private static AbstractSocketFactory currentFactory = new NormalSocketFactory();

    public static AbstractSocketFactory getSocketFactory() {
        return currentFactory;
    }

    public static void setSocketFactory(AbstractSocketFactory sf) {
        if (sf == null)
            throw new NullPointerException("Attempting to set null socket factory.");
        currentFactory = sf;
    }

    /**
     * Constructs a new socket object, appropriate to the purpose
     * of the factory.
     *
     * @param host the host name
     * @param port the port number
     * @return a socket connection object
     * @throws IOException
     * @throws UnknownHostException
     */
    public abstract SocketInterface makeSocket(String host, int port) throws IOException, UnknownHostException;

    /**
     * Constructs a new SocketInterface object, encapsulating a standard Java
     * API Socket object.
     *
     * @param socket the socket to encapsulate
     * @return a socket connection object
     * @throws IOException
     */
    public abstract SocketInterface makeSocket(Socket socket) throws IOException;

}
