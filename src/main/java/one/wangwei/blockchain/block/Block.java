package one.wangwei.blockchain.block;

import lombok.Data;
import one.wangwei.blockchain.util.ByteUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
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
    private String prevBlockHash;
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

    public Block(String hash, String prevBlockHash, String data, long timeStamp) {
        this();
        this.hash = hash;
        this.prevBlockHash = prevBlockHash;
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
     * 计算区块Hash
     * <p>
     * 注意：在准备区块数据时，一定要从原始数据类型转化为byte[]，不能直接从字符串进行转换
     *
     * @return
     */
    private void setHash() {
        byte[] prevBlockHashBytes = {};
        if (StringUtils.isNoneBlank(this.getPrevBlockHash())) {
            prevBlockHashBytes = new BigInteger(this.getPrevBlockHash(), 16).toByteArray();
        }

        byte[] headers = ByteUtils.merge(
                prevBlockHashBytes,
                this.getData().getBytes(),
                ByteUtils.toBytes(this.getTimeStamp()));

        this.setHash(DigestUtils.sha256Hex(headers));
    }
}
