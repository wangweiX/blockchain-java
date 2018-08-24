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
        // 1BjZzivUJzGRt6VqNkX8vZ3DVbVwLwETpR
        // 1NPeKwxHehoWK1SY6LqHxgVYsB7Ud65BkP
        // 1MtTNQE9t17csqMKWS6TWbweBWdGj2FgyQ
        try {
//            String[] argss = {"createwallet"};
//            String[] argss = {"createblockchain", "-address", "1BjZzivUJzGRt6VqNkX8vZ3DVbVwLwETpR"};
//            String[] argss = {"printaddresses"};
//            String[] argss = {"getbalance", "-address", "1B9eFkAq3BoeRbinryDUFaHsTAEjxzBP4S"};
//            String[] argss = {"send", "-from", "1BjZzivUJzGRt6VqNkX8vZ3DVbVwLwETpR", "-to", "1NPeKwxHehoWK1SY6LqHxgVYsB7Ud65BkP", "-amount", "1"};
            String[] argss = {"printchain"};
//
            CLI cli = new CLI(argss);
            cli.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
