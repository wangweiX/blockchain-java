package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.pow.ProofOfWork;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {

        Blockchain blockchain = Blockchain.newBlockchain();

        blockchain.addBlock("Send 1 BTC to Ivan");
        blockchain.addBlock("Send 2 more BTC to Ivan");

        for (Block block : blockchain.getBlockList()) {
            System.out.printf("Prev.hash: %s \n", block.getPrevBlockHash());
            System.out.printf("Data: %s \n", block.getData());
            System.out.printf("Hash: %s \n", block.getHash());
            System.out.printf("nonce: %s \n", block.getNonce());

            ProofOfWork pow = ProofOfWork.newProofOfWork(block);
            System.out.printf("pow: %s \n", pow.validate());
        }
    }

}
