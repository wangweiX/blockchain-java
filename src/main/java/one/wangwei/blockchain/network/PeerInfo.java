package one.wangwei.blockchain.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * P2P网络中的节点信息
 *
 * @author wangwei
 * @date 2018/08/24
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PeerInfo {
    /**
     * peer在p2p网络的唯一标示
     */
    private String id;
    /**
     * IP 地址
     */
    private String host;
    /**
     * TCP 端口
     */
    private int port;

    public PeerInfo(String host, int port) {
        this(host + ":" + port, host, port);
    }

    public PeerInfo(int port) {
        this(null, port);
    }

}
