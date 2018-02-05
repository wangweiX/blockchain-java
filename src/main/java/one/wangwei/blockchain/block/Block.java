package one.wangwei.blockchain.block;

import lombok.Data;
import one.wangwei.blockchain.pow.PowResult;
import one.wangwei.blockchain.pow.ProofOfWork;

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
     * 区块数据（交易数据）
     */
    private String data;
    /**
     * 区块创建时间(单位:秒)
     */
    private long timeStamp;
    /**
     * 工作量证明计数器
     */
    private long nonce;

    public Block() {
    }

    public Block(String hash, String prevBlockHash, String data, long timeStamp, long nonce) {
        this();
        this.hash = hash;
        this.prevBlockHash = prevBlockHash;
        this.data = data;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
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
        Block block = new Block("", previousHash, data, Instant.now().getEpochSecond(), 0);
        ProofOfWork pow = ProofOfWork.newProofOfWork(block);
        PowResult powResult = pow.run();
        block.setHash(powResult.getHash());
        block.setNonce(powResult.getNonce());
        return block;
    }
}
