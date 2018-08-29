package one.wangwei.blockchain.network.socket;

import java.io.IOException;
import java.net.Socket;

public class NormalSocketFactory extends AbstractSocketFactory {

    @Override
    public SocketInterface makeSocket(String host, int port) throws IOException {
        return new NormalSocket(host, port);
    }

    @Override
    public SocketInterface makeSocket(Socket socket) throws IOException {
        return new NormalSocket(socket);
    }
}
