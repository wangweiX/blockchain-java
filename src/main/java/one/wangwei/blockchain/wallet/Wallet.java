package one.wangwei.blockchain.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import one.wangwei.blockchain.util.Base58Check;
import one.wangwei.blockchain.util.BtcAddressUtils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.security.*;

/**
 * 钱包
 *
 * @author wangwei
 * @date 2018/03/14
 */
@Data
@AllArgsConstructor
public class Wallet implements Serializable {

    /**
     * 校验码长度
     */
    private static final int ADDRESS_CHECKSUM_LEN = 4;
    /**
     * 私钥
     */
    private PrivateKey privateKey;
    /**
     * 公钥
     */
    private PublicKey publicKey;


    public Wallet() {
        initWallet();
    }

    /**
     * 初始化钱包
     */
    private void initWallet() {
        try {
            KeyPair keyPair = newKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建新的密钥对
     *
     * @return
     * @throws Exception
     */
    private KeyPair newKeyPair() throws Exception {
        // 注册 BC Provider
        Security.addProvider(new BouncyCastleProvider());
        // 创建椭圆曲线算法的密钥对生成器，算法为 ECDSA
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        // 椭圆曲线（EC）域参数设定
        // bitcoin 为什么会选择 secp256k1，详见：https://bitcointalk.org/index.php?topic=151120.0
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }


    /**
     * 获取钱包地址
     *
     * @return
     */
    public String getAddress() throws Exception {
        // 获取 ripemdHashedKey
        byte[] ripemdHashedKey = BtcAddressUtils.ripeMD160Hash(this.getPublicKey().getEncoded());

        //3. 添加版本 0x00
        ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
        addrStream.write((byte) 0);
        addrStream.write(ripemdHashedKey);
        byte[] versionedPayload = addrStream.toByteArray();

        //4. 计算校验码
        byte[] checksum = BtcAddressUtils.checksum(versionedPayload);

        //5. 得到 version + paylod + checksum 的组合
        addrStream.write(checksum);
        byte[] binaryAddress = addrStream.toByteArray();

        //6. 执行Base58转换处理
        return Base58Check.rawBytesToBase58(binaryAddress);
    }

}
