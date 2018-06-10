package one.wangwei.blockchain;

import one.wangwei.blockchain.cli.CLI;

import java.math.BigDecimal;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {

        String str = "76a9147f9b1a7fb68d60c536c2fd8aeaa53a8f3cc025a888ac";
        System.out.println(str.length());

        String hh = "OP_DUP OP_HASH160 7f9b1a7fb68d60c536c2fd8aeaa53a8f3cc025a8 OP_EQUALVERIFY OP_CHECKSIG";
        BigDecimal bi = new BigDecimal(19);

//        try {
////            String[] argss = {"createwallet"};
////            String[] argss = {"createblockchain", "-address", "1CceyiwYXh6vL6dLPw6WiNc5ihqVxwYHSA"};
//            // 1CceyiwYXh6vL6dLPw6WiNc5ihqVxwYHSA
//            // 1G9TkDEp9YTnGa6gS5zaWkwGQwKrRykXcf
//            // 1EKacQPNxTd8N7Y83VK11zoqm7bhUZiDHm
////            String[] argss = {"printaddresses"};
////            String[] argss = {"getbalance", "-address", "1G9TkDEp9YTnGa6gS5zaWkwGQwKrRykXcf"};
//            String[] argss = {"send", "-from", "1CceyiwYXh6vL6dLPw6WiNc5ihqVxwYHSA", "-to", "1EKacQPNxTd8N7Y83VK11zoqm7bhUZiDHm", "-amount", "5"};
//            CLI cli = new CLI(argss);
//            cli.parse();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
