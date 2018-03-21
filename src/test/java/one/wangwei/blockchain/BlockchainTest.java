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
            String[] argss = {"createwallet"};
            // String[] argss = {"printaddresses"};
//             String[] argss = {"getbalance", "-address", "1Pe9yj84h5qB3hf9EGscft1LZ16uTG9JvW"};
//            String[] argss = {"send", "-from", "1Fe8DVxPKYCVR3yNUKJko1qJ5GsGBYVNvY", "-to", "1Pe9yj84h5qB3hf9EGscft1LZ16uTG9JvW", "-amount", "2"};
            CLI cli = new CLI(argss);
            cli.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
