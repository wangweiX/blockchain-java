package one.wangwei.blockchain.cli;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.pow.ProofOfWork;
import one.wangwei.blockchain.util.RocksDBUtils;
import org.apache.commons.cli.*;

/**
 * 程序命令行工具入口
 *
 * @author wangwei
 * @date 2018/03/08
 */
public class CLI {

    private String[] args;
    private Options options = new Options();

    public CLI(String[] args) {
        this.args = args;
        options.addOption("h", "help", false, "show help");
        options.addOption("add", "addblock", true, "add a block to the blockchain");
        options.addOption("print", "printchain", false, "print all the blocks of the blockchain");
    }

    /**
     * 命令行解析入口
     */
    public void parse() {
        this.validateArgs(args);
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                help();
            }
            if (cmd.hasOption("add")) {
                String data = cmd.getOptionValue("add");
                addBlock(data);
            }
            if (cmd.hasOption("print")) {
                printChain();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RocksDBUtils.getInstance().closeDB();
        }
    }


    /**
     * 验证入参
     *
     * @param args
     */
    private void validateArgs(String[] args) {
        if (args == null || args.length < 1) {
            help();
        }
    }

    /**
     * 打印帮助信息
     */
    private void help() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
        System.exit(0);
    }

    /**
     * 添加区块
     *
     * @param data
     */
    private void addBlock(String data) throws Exception {
        Blockchain blockchain = Blockchain.newBlockchain();
        blockchain.addBlock(data);
    }

    /**
     * 打印出区块链中的所有区块
     */
    private void printChain() throws Exception {
        Blockchain blockchain = Blockchain.newBlockchain();
        for (Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hashNext(); ) {
            Block block = iterator.next();

            if (block != null) {
                boolean validate = ProofOfWork.newProofOfWork(block).validate();
                System.out.println(block.toString() + ", validate = " + validate);
            }
        }
    }

}
