package one.wangwei.blockchain.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.Arrays;

/**
 * 地址工具类
 *
 * @author wangwei
 * @date 2018/03/21
 */
public class BtcAddressUtils {

    /**
     * 计算公钥的 RIPEMD160 Hash值
     *
     * @param pubKey 公钥
     * @return ipeMD160Hash(sha256 ( pubkey))
     */
    public static byte[] ripeMD160Hash(byte[] pubKey) {
        //1. 先对公钥做 sha256 处理
        byte[] shaHashedKey = DigestUtils.sha256(pubKey);
        RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
        ripemd160.update(shaHashedKey, 0, shaHashedKey.length);
        byte[] output = new byte[ripemd160.getDigestSize()];
        ripemd160.doFinal(output, 0);
        return output;
    }

    /**
     * 生成公钥的校验码
     *
     * @param payload
     * @return
     */
    public static byte[] checksum(byte[] payload) {
        byte[] firstSHA = DigestUtils.sha256(payload);
        byte[] secondSHA = DigestUtils.sha256(firstSHA);
        return Arrays.copyOfRange(secondSHA, 0, 4);
    }

    /**
     * 从比特币地址中获取 RIPEMD160 Hash 值
     *
     * @param address
     * @return
     */
    public static byte[] getRipeMD160Hash(String address) {
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        return Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
    }

}
