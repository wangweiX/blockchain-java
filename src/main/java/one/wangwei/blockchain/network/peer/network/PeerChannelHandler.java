package one.wangwei.blockchain.network.peer.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.network.peer.Config;
import one.wangwei.blockchain.network.peer.Peer;
import one.wangwei.blockchain.network.peer.network.message.Handshake;
import one.wangwei.blockchain.network.peer.network.message.Message;

@Slf4j
@Sharable
public class PeerChannelHandler extends SimpleChannelInboundHandler<Message> {

    static final String SESSION_ATTRIBUTE_KEY = "session";

    static Attribute<Connection> getSessionAttribute(ChannelHandlerContext ctx) {
        return ctx.attr(AttributeKey.<Connection>valueOf(SESSION_ATTRIBUTE_KEY));
    }

    private final Config config;

    private final Peer peer;

    public PeerChannelHandler(Config config, Peer peer) {
        this.config = config;
        this.peer = peer;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel active {}", ctx.channel().remoteAddress());
        final Connection connection = new Connection(ctx);
        getSessionAttribute(ctx).set(connection);
        ctx.writeAndFlush(new Handshake(config.getPeerName(), peer.getLeaderName()));
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel inactive {}", ctx.channel().remoteAddress());
        final Connection connection = getSessionAttribute(ctx).get();
        peer.handleConnectionClosed(connection);
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Message message) throws Exception {
        log.debug("Message {} received from {}", message.getClass(), ctx.channel().remoteAddress());
        final Connection connection = getSessionAttribute(ctx).get();
        message.handle(peer, connection);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        log.error("Channel failure " + ctx.channel().remoteAddress(), cause);
        ctx.close();
        peer.handleConnectionClosed(getSessionAttribute(ctx).get());
    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) {
        if (evt instanceof IdleStateEvent) {
            final IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.warn("Channel idle {}", ctx.channel().remoteAddress());
                ctx.close();
            }
        }
    }

}
