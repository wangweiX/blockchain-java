package one.wangwei.blockchain.network;

import com.google.common.collect.Lists;
import lombok.Data;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.network.message.MessageTypEnum;
import one.wangwei.blockchain.network.message.PeerMessage;
import one.wangwei.blockchain.network.message.data.BaseMessageData;
import one.wangwei.blockchain.network.message.handle.HandlerInterface;
import one.wangwei.blockchain.network.socket.AbstractSocketFactory;
import one.wangwei.blockchain.network.socket.SocketInterface;
import one.wangwei.blockchain.util.LoggerUtil;
import one.wangwei.blockchain.wallet.Wallet;
import one.wangwei.blockchain.wallet.WalletUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * P2P网络节点
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Data
public class Node {

    /**
     * This class is used to respond to and handle incoming connections in a separate thread.
     */
    private class PeerHandler extends Thread {

        private SocketInterface s;

        public PeerHandler(Socket socket) throws IOException {
            s = AbstractSocketFactory.getSocketFactory().makeSocket(socket);
        }

        @Override
        public void run() {
            LoggerUtil.getLogger().fine("New PeerHandler: " + s);
            PeerConnection peerConn = new PeerConnection(null, s);
            PeerMessage peerMsg = peerConn.recvData();

            if (!handlers.containsKey(peerMsg.getMsgType())) {
                LoggerUtil.getLogger().fine("Not handled: " + peerMsg);
            } else {
                LoggerUtil.getLogger().finer("Handling: " + peerMsg);
                handlers.get(peerMsg.getMsgType()).handleMessage(peerConn, peerMsg, blockchain);
            }
            LoggerUtil.getLogger().fine("Disconnecting incoming: " + peerConn);
            // NOTE: log message should indicate null peerconn host
            peerConn.close();
        }
    }

    /**
     * This class is used to set up "stabilizer" functions to run at specified intervals
     */
    private class StabilizerRunner extends Thread {
        private StabilizerInterface st;
        // milliseconds
        private int delay;

        public StabilizerRunner(StabilizerInterface st, int delay) {
            this.st = st;
            this.delay = delay;
        }

        @Override
        public void run() {
            while (true) {
                st.stabilizer();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    LoggerUtil.getLogger().fine("" + e);
                }
            }
        }
    }

    /**
     * socket 超时时间
     */
    private static final int SOCKETTIMEOUT = 2000;
    /**
     * 本地节点信息
     */
    private PeerInfo myInfo;
    /**
     * peer 数量限制。 0: 表示无限制
     */
    private int maxPeers;
    /**
     * 网络中可知的节点列表
     */
    private Hashtable<String, PeerInfo> peers;

    private Hashtable<String, HandlerInterface> handlers;
    private RouterInterface router;

    /**
     * node 模式
     */
    private boolean shutdown;

    private Blockchain blockchain;
    /**
     * 钱包地址
     */
    private String address;

    public Node(int maxPeers, PeerInfo info, String btcAddress) {
        if (StringUtils.isBlank(btcAddress)) {
            Wallet wallet = WalletUtils.getInstance().createWallet();
            btcAddress = wallet.getAddress();
        }

        this.setAddress(btcAddress);

        Blockchain blockchain = Blockchain.createBlockchain(btcAddress);

        if (info.getHost() == null) {
            info.setHost(getHostname());
        }
        if (info.getId() == null) {
            info.setId(info.getHost() + ":" + info.getPort());
        }

        this.myInfo = info;
        this.maxPeers = maxPeers;

        this.peers = new Hashtable<>();
        this.handlers = new Hashtable<>();
        this.router = null;

        this.shutdown = false;

        this.blockchain = blockchain;
    }

    public Node(int maxPeers, PeerInfo info) {
        this(maxPeers, info, "");
    }

    public Node(int port) {
        this(0, new PeerInfo(port));
    }

    /**
     * 获取本地hostname
     *
     * @return
     */
    private String getHostname() {
        String host = "";
        try {
            Socket s = new Socket("www.google.com", 80);
            host = s.getLocalAddress().getHostAddress();
        } catch (UnknownHostException e) {
            LoggerUtil.getLogger().warning("Could not determine host: " + e);
        } catch (IOException e) {
            LoggerUtil.getLogger().warning(e.toString());
        }
        LoggerUtil.getLogger().config("Determined host: " + host);
        return host;
    }

    /**
     * 创建 server socket,并监听端口port
     *
     * @param port 端口号（0 则自动分配端口）
     * @return
     * @throws IOException
     */
    public ServerSocket makeServerSocket(int port) throws IOException {
        return makeServerSocket(port, 5);
    }

    /**
     * 创建 server socket,并监听端口port
     *
     * @param port    端口号（0 则自动分配端口）
     * @param backlog 请求队列的最大长度
     * @return
     * @throws IOException
     */
    public ServerSocket makeServerSocket(int port, int backlog) throws IOException {
        ServerSocket s = new ServerSocket(port, backlog);
        s.setReuseAddress(true);
        return s;
    }

    /**
     * 通过路由的方法，发送消息到指定的节点
     *
     * @param peerId
     * @param msgType
     * @param msgData
     * @param waitReply
     * @param <T>
     * @return
     */
    public <T extends BaseMessageData> List<PeerMessage> sendToPeer(String peerId, MessageTypEnum msgType,
                                                                    T msgData, boolean waitReply) {
        PeerInfo pd = null;
        if (router != null) {
            pd = router.route(peerId);
        }
        if (pd == null) {
            LoggerUtil.getLogger().severe(String.format("Unable to route %s to %s", msgType, peerId));
            return Lists.newArrayList();
        }
        return connectAndSend(pd, msgType, msgData, waitReply);
    }

    /**
     * 连接节点并发送消息
     *
     * @param peerInfo  节点信息
     * @param msgType   消息类型
     * @param msgData   消息数据
     * @param waitReply 是否等待
     * @param <T>
     * @return
     */
    public <T extends BaseMessageData> List<PeerMessage> connectAndSend(PeerInfo peerInfo, MessageTypEnum msgType,
                                                                        T msgData, boolean waitReply) {
        List<PeerMessage> msgReply = new ArrayList<>();
        try {
            PeerConnection peerConn = new PeerConnection(peerInfo);
            PeerMessage toSend = new PeerMessage(msgType, msgData);
            peerConn.sendData(toSend);
            LoggerUtil.getLogger().fine("Sent " + toSend + "/" + peerConn);

            if (waitReply) {
                PeerMessage oneReply = peerConn.recvData();
                if (oneReply != null) {
                    msgReply.add(oneReply);
                    LoggerUtil.getLogger().fine("Got reply " + oneReply);
                }
            }
            peerConn.close();
        } catch (IOException e) {
            LoggerUtil.getLogger().warning("Error: " + e + "/" + peerInfo + "/" + msgType);
        }
        return msgReply;
    }

    /**
     * Starts the loop which is the primary operation of the Node.
     * The main loop opens a server socket, listens for incoming connections,
     * and dispatches them to registered handlers appropriately.
     */
    public void mainLoop() {
        try {
            ServerSocket s = makeServerSocket(myInfo.getPort());
            s.setSoTimeout(SOCKETTIMEOUT);
            while (!shutdown) {
                LoggerUtil.getLogger().fine("Listening...");
                try {
                    Socket clientSock = s.accept();
                    clientSock.setSoTimeout(0);
                    PeerHandler ph = new PeerHandler(clientSock);
                    ph.start();
                } catch (SocketTimeoutException e) {
                    LoggerUtil.getLogger().fine("" + e);
                }
            }
            // end while (!shutdown);
            s.close();
        } catch (SocketException e) {
            LoggerUtil.getLogger().severe("Stopping main loop (SocketExc): " + e);
        } catch (IOException e) {
            LoggerUtil.getLogger().severe("Stopping main loop (IOExc): " + e);
        }
        shutdown = true;
    }

    /**
     * Starts a "stabilizer" function running at the specified
     * interval.
     *
     * @param st    the stabilizer function object
     * @param delay the delay (in milliseconds)
     */
    public void startStabilizer(StabilizerInterface st, int delay) {
        StabilizerRunner sr = new StabilizerRunner(st, delay);
        sr.start();
    }

    public void addHandler(String msgType, HandlerInterface handler) {
        handlers.put(msgType, handler);
    }

    public void addRouter(RouterInterface router) {
        this.router = router;
    }

    public boolean addPeer(PeerInfo pd) {
        return addPeer(pd.getId(), pd);
    }

    /**
     * 增加网络节点
     *
     * @param key
     * @param pd
     * @return
     */
    public boolean addPeer(String key, PeerInfo pd) {
        boolean flag = (maxPeers == 0 || peers.size() < maxPeers) && !peers.containsKey(key);
        if (flag) {
            peers.put(key, pd);
            return true;
        }
        return false;
    }

    public PeerInfo getPeer(String key) {
        return peers.get(key);
    }

    public PeerInfo removePeer(String key) {
        return peers.remove(key);
    }

    public Set<String> getPeerKeys() {
        return peers.keySet();
    }

    public int getNumberOfPeers() {
        return peers.size();
    }

    public int getMaxPeers() {
        return maxPeers;
    }

    public boolean maxPeersReached() {
        return maxPeers > 0 && peers.size() == maxPeers;
    }

    public String getId() {
        return myInfo.getId();
    }

    public String getHost() {
        return myInfo.getHost();
    }

    public int getPort() {
        return myInfo.getPort();
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}
