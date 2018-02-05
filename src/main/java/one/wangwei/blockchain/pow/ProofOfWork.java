package one.wangwei.blockchain.pow;

import lombok.Data;
import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.util.ByteUtils;
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
    public static final int targetBits = 20;

    private Block block;
    private BigInteger target;

    private ProofOfWork(Block block, BigInteger target) {
        this.block = block;
        this.target = target;
    }

    /**
     * 创建新的工作量证明
     * <p>
     * 设定难度目标值
     *
     * @param block
     * @return
     */
    public static ProofOfWork newProofOfWork(Block block) {
        BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - targetBits));
        return new ProofOfWork(block, targetValue);
    }

    /**
     * 运行工作量证明
     *
     * @return
     */
    public PowResult run() {
        int nonce = 0;
        byte[] hash = new byte[32];

        System.out.printf("Mining the block containing：%s \n", new String(this.getBlock().getData()));
        long startTime = System.currentTimeMillis();
        while (nonce < Integer.MAX_VALUE) {
            byte[] bytes = prepareData(nonce);

            hash = DigestUtils.sha256(bytes);

            BigInteger hashInt = new BigInteger(hash);
            if (hashInt.compareTo(this.target) == -1) {
                System.out.printf("Elapsed Time: %s seconds \n\n", (float) (System.currentTimeMillis() - startTime) / 1000);
                break;
            } else {
                nonce++;
            }
        }
        return new PowResult(nonce, hash);
    }

    /**
     * <p> 验证区块是否有效 </p>
     *
     * @return
     */
    public boolean validate() {
        byte[] data = this.prepareData(this.getBlock().getNonce());
        byte[] hash = DigestUtils.sha256(data);
        return new BigInteger(hash).compareTo(this.target) == -1;
    }


    /**
     * 准备数据
     *
     * @param nonce
     * @return
     */
    private byte[] prepareData(int nonce) {
        return ByteUtils.merge(
                this.getBlock().getPrevBlockHash(),
                this.getBlock().getData(),
                ByteUtils.toByte(this.getBlock().getTimeStamp()),
                ByteUtils.toByte(targetBits),
                ByteUtils.toByte(nonce)
        );
    }

}
