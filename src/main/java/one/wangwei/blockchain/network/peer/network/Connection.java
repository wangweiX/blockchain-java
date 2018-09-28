package one.wangwei.blockchain.network.peer.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.network.peer.network.message.Message;

import java.net.InetSocketAddress;

/**
 * Maintains a TCP connection between the local peer and a neighbour
 */
@Slf4j
public class Connection {

    private final InetSocketAddress remoteAddress;

    private ChannelHandlerContext ctx;

    private String peerName;

    public Connection(ChannelHandlerContext ctx) {
        this.remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        this.ctx = ctx;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(final String peerName) {
        if (this.peerName == null) {
            this.peerName = peerName;
        } else {
            log.warn("peer name {} set again for connection {}", peerName, this);
        }
    }

    public void send(final Message msg) {
        if (ctx != null) {
            ctx.writeAndFlush(msg);
        } else {
            log.error("Can not send message " + msg.getClass() + " to " + toString());
        }
    }

    public void close() {
        log.debug("Closing session of {}", toString());
        if (ctx != null) {
            ctx.close();
            ctx = null;
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        Connection that = (Connection) other;

        return !(peerName != null ? !peerName.equals(that.peerName) : that.peerName != null);
    }

    @Override
    public int hashCode() {
        return peerName != null ? peerName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "remoteAddress=" + remoteAddress +
                ", isOpen=" + (ctx != null) +
                ", peerName='" + peerName + '\'' +
                '}';
    }
}
