package one.wangwei.blockchain;

import one.wangwei.blockchain.cli.CLI;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {
        try {
//            String[] argss = {"createwallet"};
//             String[] argss = {"printaddresses"};
             String[] argss = {"getbalance", "-address", "1A2SURFiuJjGDb2BSCAkae4zR4Aw1Ac5t7"};
//            String[] argss = {"send", "-from", "13fheAYPZ1VHAjGM6ZXT9siywGmD4UUf8e", "-to", "1A2SURFiuJjGDb2BSCAkae4zR4Aw1Ac5t7", "-amount", "2"};
            CLI cli = new CLI(argss);
            cli.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
