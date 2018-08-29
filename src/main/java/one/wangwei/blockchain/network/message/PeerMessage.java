package one.wangwei.blockchain.network.message;


import lombok.ToString;
import one.wangwei.blockchain.network.socket.SocketInterface;
import one.wangwei.blockchain.util.ByteUtils;

import java.io.IOException;


/**
 * 用于传递消息类型与消息数据的对象
 *
 * @author wangwei
 * @date 2018/08/24
 */
@ToString
public class PeerMessage<T extends MessageData> {

    /**
     * 规定message type转化后的byte[]长度
     */
    public final static int MESSAGE_TYPE_DATA_LENGTH = 12;

    /**
     * 消息类型
     */
    private byte[] type;
    /**
     * 数据
     */
    private byte[] data;

    public PeerMessage(byte[] type, byte[] data) {
        this.type = type.clone();
        this.data = data.clone();
    }

    public PeerMessage(MessageTypEnum messageType, T messageData) {
        this(messageType.getTypeBytes(), messageData.toBytes());
    }

    /**
     * 从socket连接中读取返回的信息
     *
     * @param s
     * @throws IOException
     */
    public PeerMessage(SocketInterface s) throws IOException {
        type = new byte[MESSAGE_TYPE_DATA_LENGTH];
        // 消息数据长度 byte array
        byte[] dataLen = new byte[4];

        if (s.read(type) != MESSAGE_TYPE_DATA_LENGTH) {
            throw new IOException("EOF in PeerMessage constructor: type");
        }
        if (s.read(dataLen) != 4) {
            throw new IOException("EOF in PeerMessage constructor: dataLen");
        }
        int len = ByteUtils.toInt(dataLen);
        data = new byte[len];

        if (s.read(data) != len) {
            throw new IOException("EOF in PeerMessage constructor: Unexpected message data length");
        }
    }

    /**
     * 以字节数组的形式返回此消息的压缩表示形式：
     * type + data length + data
     *
     * @return
     */
    public byte[] toBytes() {
        byte[] bytes = new byte[MESSAGE_TYPE_DATA_LENGTH + 4 + data.length];
        byte[] dataLenBytes = ByteUtils.toBytes(data.length);

        // 保存消息类型byte数组
        for (int i = 0; i < MESSAGE_TYPE_DATA_LENGTH; i++) bytes[i] = type[i];
        // 保存消息数据长度byte数组
        for (int i = 0; i < 4; i++) bytes[i + MESSAGE_TYPE_DATA_LENGTH] = dataLenBytes[i];
        // 保存消息数组byte数组
        for (int i = 0; i < data.length; i++) bytes[i + 4 + MESSAGE_TYPE_DATA_LENGTH] = data[i];

        return bytes;
    }

    /**
     * 返回字符串形式的消息类型
     *
     * @return
     */
    public String getMsgType() {
        return MessageTypEnum.bytesToType(getMsgTypeBytes());
    }

    public byte[] getMsgTypeBytes() {
        return type.clone();
    }

    public byte[] getMsgDataBytes() {
        return data.clone();
    }

}
