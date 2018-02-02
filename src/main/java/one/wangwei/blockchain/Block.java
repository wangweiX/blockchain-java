package one.wangwei.blockchain;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 区块
 *
 * @author wangwei
 * @date 2018/02/02
 */
@Data
public class Block {

    /**
     * 区块hash值
     */
    private String hash;
    /**
     * 前一个区块的hash值
     */
    private String previousHash;
    /**
     * 区块数据
     */
    private String data;
    /**
     * 区块创建时间戳
     */
    private long timeStamp;

    /**
     * @param previousHash 前一个区块hash值
     * @param data         该区块数据
     */
    Block(String previousHash, String data) {
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = System.currentTimeMillis();
        this.hash = this.calculateHash();
    }

    /**
     * <p> 计算区块Hash </p>
     * 对 previousHash + timeStamp + data 进行hash计算
     *
     * @return
     */
    private String calculateHash() {
        return DigestUtils.sha256Hex(previousHash + Long.toString(timeStamp) + data);
    }

    /**
     * <p> 创建创世区块 </p>
     *
     * @return
     */
    public static Block newGenesisBlock() {
        return new Block("0", "Genesis Block");
    }
}
