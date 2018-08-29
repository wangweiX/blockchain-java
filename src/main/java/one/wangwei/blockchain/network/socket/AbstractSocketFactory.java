package one.wangwei.blockchain.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * socket 抽象工厂
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
     * 创建socket
     *
     * @param host
     * @param port
     * @return
     * @throws IOException
     * @throws UnknownHostException
     */
    public abstract SocketInterface makeSocket(String host, int port) throws IOException, UnknownHostException;

    /**
     * 创建socket
     *
     * @param socket
     * @return
     * @throws IOException
     */
    public abstract SocketInterface makeSocket(Socket socket) throws IOException;

}
