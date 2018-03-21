package one.wangwei.blockchain.wallet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 钱包工具类
 *
 * @author wangwei
 * @date 2018/03/21
 */
public class WalletUtils {

    /**
     * 钱包工具实例
     */
    private volatile static WalletUtils instance;

    public static WalletUtils getInstance() {
        if (instance == null) {
            synchronized (WalletUtils.class) {
                if (instance == null) {
                    instance = new WalletUtils();
                }
            }
        }
        return instance;
    }

    private WalletUtils() {
        initWalletFile();
    }

    /**
     * 钱包文件
     */
    private final static String WALLET_FILE = "wallet.dat";
    /**
     * 钱包数据
     */
    private Map<String, Wallet> walletMap = new HashMap<>();

    /**
     * 初始化钱包文件
     */
    private void initWalletFile() {
        File file = new File(WALLET_FILE);
        if (!file.exists()) {
            this.saveToDisk();
        } else {
            this.loadFromDisk();
        }
    }

    /**
     * 获取所有的钱包地址
     *
     * @return
     * @throws Exception
     */
    public Set<String> getAddresses() throws Exception {
        if (walletMap == null) {
            throw new Exception("ERROR: Fail to get addresses ! There isn't address ! ");
        }
        return walletMap.keySet();
    }

    /**
     * 获取钱包数据
     *
     * @param address 钱包地址
     * @return
     */
    public Wallet getWallet(String address) throws Exception {
        if (StringUtils.isBlank(address)) {
            throw new Exception("ERROR: Fail to get wallet ! address invalid ! ");
        }
        Wallet wallet = walletMap.get(address);
        if (wallet == null) {
            throw new Exception("ERROR: Fail to get wallet ! wallet don't exist ! ");
        }
        return wallet;
    }

    /**
     * 创建钱包
     *
     * @return
     */
    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        this.addWallet(wallet);
        return wallet;
    }

    /**
     * 添加钱包
     *
     * @param wallet
     */
    private void addWallet(Wallet wallet) {
        try {
            this.walletMap.put(wallet.getAddress(), wallet);
            this.saveToDisk();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存钱包数据
     */
    private void saveToDisk() {
        try {
            if (this.walletMap == null) {
                throw new Exception("ERROR: Fail to save wallet to file ! There isn't data in wallet maps. ");
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            new ObjectOutputStream(buffer).writeObject(walletMap);
            FileUtils.writeByteArrayToFile(new File(WALLET_FILE), buffer.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载钱包数据
     */
    private void loadFromDisk() {
        try {
            File file = new File(WALLET_FILE);
            if (!file.exists() || !file.isFile()) {
                throw new Exception("ERROR: Fail to load wallet from disk ! file don't exist or isn't a file !");
            }
            byte[] walletsBytes = FileUtils.readFileToByteArray(file);
            ByteArrayInputStream buffer = new ByteArrayInputStream(walletsBytes);
            this.walletMap = (Map<String, Wallet>) new ObjectInputStream(buffer).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
