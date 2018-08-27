package one.wangwei.blockchain.network.message;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息类型枚举
 *
 * @author wangwei
 * @date 2018/08/24
 */
public enum MessageTypEnum {

    PING("PING", ""),
    JOIN("JOIN", ""),
    VERSION("VERSION", ""),
    TX("TX", "交易消息"),
    GETDATA("GETDATA", "获取数据"),
    GETBLOCKS("GETBLOCKS", "获取区块链Hash列表"),
    INVENTORY("INV", "交换库存清单消息"),
    BLOCK("BLOCK", "区块数据"),
    ADDR("ADDR", ""),

    ERROR("ERRO", "发送错误消息"),
    REPLY("REPLY", "发送回复消息");

    public final String type;
    public final String desc;

    MessageTypEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static MessageTypEnum getMessgeType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new RuntimeException("");
        }
        for (MessageTypEnum typEnum : MessageTypEnum.values()) {
            if (typEnum.type.equals(type)) {
                return typEnum;
            }
        }
        throw new RuntimeException("");
    }

    /**
     * 获取类型的byte数组
     *
     * @return
     */
    public byte[] getTypeBytes() {
        return typeToBytes(this.type);
    }

    /**
     * 将消息类型字符串转化长度为12的字节数组
     *
     * @param type
     * @return
     */
    public static byte[] typeToBytes(String type) {
        byte[] byteArray = new byte[PeerMessage.MESSAGE_TYPE_DATA_LENGTH];
        for (int i = 0; i < type.getBytes().length; i++) {
            byteArray[i] = type.getBytes()[i];
        }
        return byteArray;
    }

    /**
     * 消息类型byte[] 转化为字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesToType(byte[] bytes) {
        byte[] typeBytes = ArrayUtils.removeAllOccurences(bytes, (byte) 0);
        return new String(typeBytes);
    }

}
