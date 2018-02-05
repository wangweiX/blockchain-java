package one.wangwei.blockchain.block;

import lombok.Data;
import one.wangwei.blockchain.util.ByteUtils;
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
    private byte[] hash;
    /**
     * 前一个区块的hash值
     */
    private byte[] previousHash;
    /**
     * 区块数据
     */
    private byte[] data;
    /**
     * 区块创建时间(单位:秒)
     */
    private long timeStamp;

    public Block() {
    }

    public Block(byte[] hash, byte[] previousHash, byte[] data, long timeStamp) {
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
        return Block.newBlock(new byte[]{}, "Genesis Block");
    }

    /**
     * <p> 创建新区块 </p>
     *
     * @param previousHash
     * @param data
     * @return
     */
    public static Block newBlock(byte[] previousHash, String data) {
        Block block = new Block(new byte[]{}, previousHash, data.getBytes(), Instant.now().getEpochSecond());
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
        byte[] headers = ByteUtils.merge(previousHash, data, Long.toString(timeStamp).getBytes());
        this.hash = DigestUtils.sha256(headers);
    }
}
