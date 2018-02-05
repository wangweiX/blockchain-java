package one.wangwei.blockchain.block;

import lombok.Data;
import one.wangwei.blockchain.pow.PowResult;
import one.wangwei.blockchain.pow.ProofOfWork;

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
    /**
     * 工作量证明计数器
     */
    private long nonce;

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
    static Block newBlock(byte[] previousHash, String data) {
        Block block = new Block(previousHash, data.getBytes());
        ProofOfWork pow = ProofOfWork.newProofOfWork(block);
        PowResult powResult = pow.run();
        block.setHash(powResult.getHash());
        block.setNonce(powResult.getNonce());
        return block;
    }
}
