package one.wangwei.blockchain.network.peer;

import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import one.wangwei.blockchain.network.peer.network.PeerChannelHandler;
import one.wangwei.blockchain.network.peer.network.PeerChannelInitializer;
import one.wangwei.blockchain.network.peer.service.ConnectionService;
import one.wangwei.blockchain.network.peer.service.LeadershipService;
import one.wangwei.blockchain.network.peer.service.PingService;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class PeerHandle {

    private final Config config;

    private final int portToBind;

    private final EventLoopGroup acceptorEventLoopGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup networkEventLoopGroup = new NioEventLoopGroup(6);

    private final EventLoopGroup peerEventLoopGroup = new NioEventLoopGroup(1);

    private final ObjectEncoder encoder = new ObjectEncoder();

    private final Peer peer;

    private Future keepAliveFuture;

    private Future timeoutPingsFuture;

    public PeerHandle(Config config, int portToBind) {
        this.config = config;
        this.portToBind = portToBind;
        final ConnectionService connectionService = new ConnectionService(config, networkEventLoopGroup, peerEventLoopGroup, encoder);
        final LeadershipService leadershipService = new LeadershipService(connectionService, config, peerEventLoopGroup);
        final PingService pingService = new PingService(connectionService, leadershipService, config);
        this.peer = new Peer(config, connectionService, pingService, leadershipService);
    }

    public String getPeerName() {
        return config.getPeerName();
    }

    public ChannelFuture start() throws InterruptedException {
        ChannelFuture closeFuture = null;

        final PeerChannelHandler peerChannelHandler = new PeerChannelHandler(config, peer);
        final PeerChannelInitializer peerChannelInitializer = new PeerChannelInitializer(config, encoder,
                peerEventLoopGroup, peerChannelHandler);
        final ServerBootstrap peerBootstrap = new ServerBootstrap();
        peerBootstrap.group(acceptorEventLoopGroup, networkEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(peerChannelInitializer);

        final ChannelFuture bindFuture = peerBootstrap.bind(portToBind).sync();

        if (bindFuture.isSuccess()) {
            log.info("{} Successfully bind to {}", config.getPeerName(), portToBind);
            final Channel serverChannel = bindFuture.channel();

            final SettableFuture<Void> setServerChannelFuture = SettableFuture.create();
            peerEventLoopGroup.execute(() -> {
                try {
                    peer.setBindChannel(serverChannel);
                    setServerChannelFuture.set(null);
                } catch (Exception e) {
                    setServerChannelFuture.setException(e);
                }
            });

            try {
                setServerChannelFuture.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Couldn't set bind channel to server " + config.getPeerName(), e);
                System.exit(-1);
            }

            final int initialDelay = Peer.RANDOM.nextInt(config.getKeepAlivePeriodSeconds());

            this.keepAliveFuture = peerEventLoopGroup.scheduleAtFixedRate(peer::keepAlivePing, initialDelay, config.getKeepAlivePeriodSeconds(), SECONDS);

            this.timeoutPingsFuture = peerEventLoopGroup.scheduleAtFixedRate(peer::timeoutPings, 0, 100, TimeUnit.MILLISECONDS);

            closeFuture = serverChannel.closeFuture();
        } else {
            log.error(config.getPeerName() + " could not bind to " + portToBind, bindFuture.cause());
            System.exit(-1);
        }
        return closeFuture;
    }

    public CompletableFuture<Collection<String>> ping() {
        final CompletableFuture<Collection<String>> future = new CompletableFuture<>();
        peerEventLoopGroup.execute(() -> peer.ping(future));
        return future;
    }

    public CompletableFuture<Void> leave() {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        peerEventLoopGroup.execute(() -> peer.leave(future));
        if (keepAliveFuture != null && timeoutPingsFuture != null) {
            keepAliveFuture.cancel(false);
            timeoutPingsFuture.cancel(false);
            keepAliveFuture = null;
            timeoutPingsFuture = null;
        }
        return future;
    }

    public void scheduleLeaderElection() {
        peerEventLoopGroup.execute(peer::scheduleElection);
    }

    public CompletableFuture<Void> connect(final String host, final int port) {
        final CompletableFuture<Void> connectToHostFuture = new CompletableFuture<>();
        peerEventLoopGroup.execute(() -> peer.connectTo(host, port, connectToHostFuture));
        return connectToHostFuture;
    }

    public void disconnect(final String peerName) {
        peerEventLoopGroup.execute(() -> peer.disconnect(peerName));
    }

}
