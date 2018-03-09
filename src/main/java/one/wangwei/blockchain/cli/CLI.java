package one.wangwei.blockchain.cli;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.pow.ProofOfWork;
import one.wangwei.blockchain.transaction.TXOutput;
import one.wangwei.blockchain.transaction.Transaction;
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

        Option helpCmd = Option.builder("h").desc("show help").build();

        Option printchainCmd = Option.builder("printchain")
                .desc("Print all the blocks of the blockchain")
                .build();

        Option sendCmd = Option.builder("send")
                .argName("from").hasArg(true)
                .argName("to").hasArg(true)
                .argName("amount").hasArg(true)
                .numberOfArgs(3)
                .desc("Send AMOUNT of coins from FROM address to TO")
                .build();

        Option from = Option.builder("from").argName("address").build();



        Option to = Option.builder("to").argName("address").build();
        Option address = Option.builder("address").argName("address").build();

        Option getBalanceCmd = Option.builder("getBalance")
                .argName("address").hasArg(true)
                .desc("Get balance of ADDRESS")
                .build();

        Option createblockchainCmd = Option.builder("createblockchain")
                .argName("address").hasArg(true)
                .desc("Create a blockchain and send genesis block reward to ADDRESS")
                .build();


        OptionGroup optionGroup = new OptionGroup();

        options.addOption(helpCmd)
                .addOption(printchainCmd)
                .addOption(sendCmd)
                .addOption(createblockchainCmd)
                .addOption(getBalanceCmd)
                .addOption(from)
                .addOption(to)
                .addOption(address);
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
            if (cmd.hasOption("address")) {

            }
            if (cmd.hasOption("getBalance")) {
                String[] args = cmd.getArgs();
                if (args == null || args.length < 1) {
                    help();
                }
                for (String arg : args) {
                    System.out.println(arg);
                }
            }
            if (cmd.hasOption("createblockchain")) {
                String address = cmd.getOptionValue("createblockchain");
                Blockchain.createBlockchain(address);
            }
            if (cmd.hasOption("print")) {
//                printChain();
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
     * 创建区块链
     *
     * @param address
     */
    private void createBlockchain(String address) throws Exception {
        Blockchain.createBlockchain(address);
        RocksDBUtils.getInstance().closeDB();
        System.out.println("Done ! ");
    }

    /**
     * 查询钱包余额
     *
     * @param address 钱包地址
     */
    private void getBalance(String address) throws Exception {
        Blockchain blockchain = Blockchain.createBlockchain(address);
        TXOutput[] txOutputs = blockchain.findUTXO(address);
        int balance = 0;
        if (txOutputs != null && txOutputs.length > 0) {
            for (TXOutput txOutput : txOutputs) {
                balance += txOutput.getValue();
            }
        }
        System.out.printf("Balance of '%s': %d\n", address, balance);
    }

    /**
     * 转账
     *
     * @param from
     * @param to
     * @param amount
     */
    private void send(String from, String to, int amount) throws Exception {
        Blockchain blockchain = Blockchain.createBlockchain(from);
        Transaction transaction = Transaction.newUTXOTransaction(from, to, amount, blockchain);
        blockchain.mineBlock(new Transaction[]{transaction});
        RocksDBUtils.getInstance().closeDB();
        System.out.println("Success!");
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
     * 打印出区块链中的所有区块
     */
    private void printChain(String address) throws Exception {
        Blockchain blockchain = Blockchain.createBlockchain(address);
        for (Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hashNext(); ) {
            Block block = iterator.next();
            if (block != null) {
                boolean validate = ProofOfWork.newProofOfWork(block).validate();
                System.out.println(block.toString() + ", validate = " + validate);
            }
        }
    }


}
