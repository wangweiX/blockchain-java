package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import org.apache.commons.codec.binary.Hex;

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
            System.out.printf("Prev. hash: %s\n", Hex.encodeHexString(block.getPreviousHash()));
            System.out.printf("Data: %s\n", new String(block.getData()));
            System.out.printf("Hash: %s\n", Hex.encodeHexString(block.getHash()));
            System.out.println("\n");
        }
    }

}
