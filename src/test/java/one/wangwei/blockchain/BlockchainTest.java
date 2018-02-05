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

//        BigInteger a = BigInteger.valueOf(1);
//        BigInteger b = a.shiftLeft(253);
//
//        // 10进制格式输出
//        System.out.println("a=" + a);
//        System.out.println("b=" + b);
//
//        // 16进制格式输出
//        System.out.println("a_hex=" + Hex.encodeHexString(a.toByteArray()));
//        System.out.println("b_hex=" + Hex.encodeHexString(b.toByteArray()));
//
//        // 二进制格式输出
//        System.out.println("a_binary=" + a.toString(2));
//        System.out.println("b_binary=" + b.toString(2));
//        System.out.println("b_binary length=" + b.toString(2).length());

        Blockchain blockchain = Blockchain.newBlockchain();
        for (int i = 0; i < 10; i++) {
            blockchain.addBlock("I'm Block and my Block num is " + i);
        }

//        for (Block block : blockchain.getBlockList()) {
//            String previousHash = block.getPreviousHash();
//            String data = block.getData();
//            String hash = block.getHash();
//            System.out.printf("previousHash= %s\n data= %s\n hash= %s\n\n", previousHash, data, hash);
//        }

    }
}
