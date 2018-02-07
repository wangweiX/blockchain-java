package one.wangwei.blockchain.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * 序列化工具类
 *
 * @author wangwei
 * @date 2018/02/07
 */
public class SerializeUtils {

    /**
     * 反序列化
     *
     * @param bytes 对象对应的字节数组
     * @return
     * @throws Exception
     */
    public static Object deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        Object obj = new Kryo().readClassAndObject(input);
        input.close();
        return obj;
    }

    /**
     * 反序列化
     *
     * @param bytes 对象对应的字节数组
     * @return
     * @throws Exception
     */
    public static List deserialize(byte[][] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new ArrayList(0);
        }
        List objects = new ArrayList<>();
        for (byte[] data : bytes) {
            objects.add(deserialize(data));
        }
        return objects;
    }

    /**
     * 序列化
     *
     * @param object 需要序列化的对象
     * @return
     * @throws Exception
     */
    public static byte[] serialize(Object object) {
        Output output = new Output(4096, -1);
        new Kryo().writeClassAndObject(output, object);
        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    /**
     * 序列化
     *
     * @param objects 需要序列化的对象
     * @return
     * @throws Exception
     */
    public static List<byte[]> serialize(Object... objects) {
        if (objects == null || objects.length == 0) {
            return new ArrayList<>(0);
        }
        List<byte[]> bytes = new ArrayList<>();
        for (Object obj : objects) {
            bytes.add(serialize(obj));
        }
        return bytes;
    }
}
