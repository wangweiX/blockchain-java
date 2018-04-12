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
            String[] argss = {"createblockchain", "-address", "1QCvr4j8QFrNfTKZtakGXmvwF25yaSjLjc"};
            // 1Ac2d6pe7EvbUzmDuTvQqF4vWKkgVoN75m
            // 1LZfi7LKXiTrm5hUwJYWSjj1Rn4mKvm4zY
            // 1ErBCVGzEXKminTvxCbZXZhgX7dLKekAFc
//            String[] argss = {"printaddresses"};
//            String[] argss = {"getbalance", "-address", "14PDYP4P3tYgVBcnALz5B1xBLFmck2ADnN"};
//            String[] argss = {"send", "-from", "1QCvr4j8QFrNfTKZtakGXmvwF25yaSjLjc", "-to", "198GXjTzTjHRN4VM9uCRwDybMeDyR832aV", "-amount", "1"};
            CLI cli = new CLI(argss);
            cli.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
