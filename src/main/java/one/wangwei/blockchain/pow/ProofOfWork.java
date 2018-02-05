package one.wangwei.blockchain.pow;

import lombok.Data;
import one.wangwei.blockchain.block.Block;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;

/**
 * 工作量证明
 *
 * @author wangwei
 * @date 2018/02/04
 */
@Data
public class ProofOfWork {

    /**
     * 难度目标值
     */
    public static final int TARGET_BITS = 21;

    private Block block;
    private BigInteger target;

    private ProofOfWork(Block block, BigInteger target) {
        this.block = block;
        this.target = target;
    }

    /**
     * 创建新的工作量证明，设定难度目标值
     *
     * @param block
     * @return
     */
    public static ProofOfWork newProofOfWork(Block block) {
        BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS));
        return new ProofOfWork(block, targetValue);
    }

    /**
     * 运行工作量证明，开始挖矿
     *
     * @return
     */
    public PowResult run() {
        long nonce = 0;
        String shaHex = "";
        System.out.printf("Mining the block containing：%s \n", this.getBlock().getData());

        long startTime = System.currentTimeMillis();
        while (nonce < Long.MAX_VALUE) {
            String data = this.prepareData(nonce);
            shaHex = DigestUtils.sha256Hex(data);
            if (new BigInteger(shaHex, 16).compareTo(this.target) == -1) {
                System.out.printf("Elapsed Time: %s seconds \n", (float) (System.currentTimeMillis() - startTime) / 1000);
                System.out.printf("correct hash Hex: %s \n\n", shaHex);
                break;
            } else {
                nonce++;
            }
        }
        return new PowResult(nonce, shaHex);
    }

    /**
     * <p> 验证区块是否有效 </p>
     *
     * @return
     */
    public boolean validate() {
        String data = this.prepareData(this.getBlock().getNonce());
        return new BigInteger(DigestUtils.sha256Hex(data), 16).compareTo(this.target) == -1;
    }

    /**
     * 准备数据
     *
     * @param nonce
     * @return
     */
    private String prepareData(long nonce) {
        return this.getBlock().getPrevBlockHash()
                + this.getBlock().getData()
                + this.getBlock().getTimeStamp()
                + TARGET_BITS
                + nonce;
    }

}
