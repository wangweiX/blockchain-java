package one.wangwei.blockchain.pow;

import lombok.Data;
import one.wangwei.blockchain.block.Block;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    public static final int targetBits = 50;

    private Block block;
    private BigInteger target;

    private ProofOfWork(Block block, BigInteger target) {
        this.block = block;
        this.target = target;
    }

    /**
     * 创建新的工作量证明
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
        long nonce = 0;
        byte[] hash = new byte[0];

        System.out.printf("Mining the block containing：%s \n", new String(this.getBlock().getData()));
        long startTime = System.currentTimeMillis();
        while (nonce < Integer.MAX_VALUE) {
            byte[] bytes = prepareData(nonce);

            System.out.println(DigestUtils.sha256Hex(bytes));

            hash = DigestUtils.sha256(bytes);
            System.out.printf("%s \n", Hex.encodeHexString(hash));
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
    private byte[] prepareData(long nonce) {
        String str = new String(this.getBlock().getPreviousHash()) +
                new String(this.getBlock().getData()) +
                this.getBlock().getTimeStamp() +
                nonce;
        return str.getBytes();
//        byte[][] bytes = {
//                this.getBlock().getPreviousHash(),
//                this.getBlock().getData(),
//                BigInteger.valueOf(this.getBlock().getTimeStamp()).toByteArray(),
//                BigInteger.valueOf(targetBits).toByteArray(),
//                BigInteger.valueOf(nonce).toByteArray()
//        };
//        return join(bytes);
    }

    /**
     * 二维数组转一维数组
     *
     * @param bytes
     * @return
     */
    private byte[] join(byte[][] bytes) {
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                list.add(bytes[i][j]);
            }
        }
        byte[] result = new byte[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }


}
