package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.pow.ProofOfWork;
import one.wangwei.blockchain.util.RedisUtils;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {

//        testRedis();

        Blockchain blockchain = Blockchain.newBlockchain();

        blockchain.addBlock("Send 1 BTC to Ivan");
        blockchain.addBlock("Send 2 more BTC to Ivan");

        for (Block block : blockchain.getBlockList()) {
            System.out.println("Prev.hash: " + block.getPrevBlockHash());
            System.out.println("Data: " + block.getData());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Nonce: " + block.getNonce());

            ProofOfWork pow = ProofOfWork.newProofOfWork(block);
            System.out.println("Pow valid: " + pow.validate() + "\n");
        }
    }

    private static void testRedis() {
        RedisUtils.getInstance().putObject("1", "wangwei");
        String val = (String) RedisUtils.getInstance().getObject("1");
        System.out.println(val);
    }

}
