package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.pow.ProofOfWork;
import one.wangwei.blockchain.util.ByteUtils;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {

        test();

        Blockchain blockchain = Blockchain.newBlockchain();

        blockchain.addBlock("Send 1 BTC to Ivan");
        blockchain.addBlock("Send 2 more BTC to Ivan");

        for (Block block : blockchain.getBlockList()) {
            byte[] prevBlockHash = block.getPrevBlockHash();
            byte[] data = block.getData();
            byte[] hash = block.getHash();

            System.out.println("Prev. hash:" + Hex.encodeHexString(prevBlockHash));
            System.out.println("Data:" + new String(data));
            System.out.println("Hash:" + Hex.encodeHexString(hash));

            ProofOfWork pow = ProofOfWork.newProofOfWork(block);
            boolean validate = pow.validate();
            System.out.println("pow: " + validate);
        }
    }


    private static void test() {
        long time = 1517810692;
        byte[] bytes = ByteUtils.toByte(time);
        System.out.println(bytes);
    }


    private static void test1() {
        BigInteger a = BigInteger.valueOf(1);
        BigInteger b = a.shiftLeft(253);

        // 10进制格式输出
        System.out.println("a=" + a);
        System.out.println("b=" + b);

        // 16进制格式输出
        System.out.println("a_hex=" + Hex.encodeHexString(a.toByteArray()));
        System.out.println("b_hex=" + Hex.encodeHexString(b.toByteArray()));

        // 二进制格式输出
        System.out.println("a_binary=" + a.toString(2));
        System.out.println("b_binary=" + b.toString(2));
        System.out.println("b_binary length=" + b.toString(2).length());
    }

}
