package one.wangwei.blockchain.block;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;

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
     * 区块创建时间(单位:秒)
     */
    private long timeStamp;

    public Block() {
    }

    public Block(String hash, String previousHash, String data, long timeStamp) {
        this();
        this.hash = hash;
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = timeStamp;
    }

    /**
     * <p> 创建创世区块 </p>
     *
     * @return
     */
    public static Block newGenesisBlock() {
        return Block.newBlock("", "Genesis Block");
    }

    /**
     * <p> 创建新区块 </p>
     *
     * @param previousHash
     * @param data
     * @return
     */
    public static Block newBlock(String previousHash, String data) {
        Block block = new Block("", previousHash, data, Instant.now().getEpochSecond());
        block.setHash();
        return block;
    }

    /**
     * <p> 计算区块Hash </p>
     * 对 previousHash + timeStamp + data 进行hash计算
     *
     * @return
     */
    private void setHash() {
        String headers = previousHash + data + timeStamp;
        this.hash = DigestUtils.sha256Hex(headers);
    }
}
