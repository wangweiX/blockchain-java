package one.wangwei.blockchain;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {
        try {
            int[] a = {};
            for (int i = 0; i < 100; i++) {
                a = ArrayUtils.add(a, i);
            }
            System.out.println(a);


//            Blockchain blockchain = Blockchain.newBlockchain("wangwei");

//            blockchain.addBlock("Send 1.0 BTC to wangwei");
//            blockchain.addBlock("Send 2.5 more BTC to wangwei");
//            blockchain.addBlock("Send 3.5 more BTC to wangwei");

//            for (Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hashNext(); ) {
//                Block block = iterator.next();
//
//                if (block != null) {
//                    boolean validate = ProofOfWork.newProofOfWork(block).validate();
//                    System.out.println(block.toString() + ", validate = " + validate);
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
