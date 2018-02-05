package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Blockchain;

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
        blockchain.addBlock("Send 1 BTC to Ivan");
        blockchain.addBlock("Send 2 more BTC to Ivan");

//        for (Block block : blockchain.getBlockList()) {
//            String previousHash = block.getPreviousHash();
//            String data = block.getData();
//            String hash = block.getHash();
//            System.out.printf("previousHash= %s\n data= %s\n hash= %s\n\n", previousHash, data, hash);
//        }

    }
}
