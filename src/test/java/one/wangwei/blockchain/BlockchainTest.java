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
        // 1B9eFkAq3BoeRbinryDUFaHsTAEjxzBP4S
        // 1Hj3SiVsDu4rqQux8yS1zTiA9uCKyvqp4
        // 138gSV2EckNufLjEjLEUBJxiF6UDx46KaL
        try {
//            String[] argss = {"createwallet"};
//            String[] argss = {"createblockchain", "-address", "1B9eFkAq3BoeRbinryDUFaHsTAEjxzBP4S"};
//            String[] argss = {"printaddresses"};
            String[] argss = {"printchain"};
//            String[] argss = {"getbalance", "-address", "1B9eFkAq3BoeRbinryDUFaHsTAEjxzBP4S"};
//            String[] argss = {"send", "-from", "1B9eFkAq3BoeRbinryDUFaHsTAEjxzBP4S", "-to", "138gSV2EckNufLjEjLEUBJxiF6UDx46KaL", "-amount", "1"};
            CLI cli = new CLI(argss);
            cli.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
