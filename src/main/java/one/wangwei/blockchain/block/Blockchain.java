package one.wangwei.blockchain.block;

import lombok.Getter;
import one.wangwei.blockchain.transaction.TXInput;
import one.wangwei.blockchain.transaction.TXOutput;
import one.wangwei.blockchain.transaction.Transaction;
import one.wangwei.blockchain.util.RocksDBUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 区块链 </p>
 *
 * @author wangwei
 * @date 2018/02/02
 */
public class Blockchain {

    @Getter
    private String lastBlockHash;

    private Blockchain(String lastBlockHash) {
        this.lastBlockHash = lastBlockHash;
    }

    /**
     * <p> 创建区块链 </p>
     *
     * @param address 钱包地址
     * @return
     */
    public static Blockchain createBlockchain(String address) throws Exception {
        String lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
        if (StringUtils.isBlank(lastBlockHash)) {
            // 创建 coinBase 交易
            Transaction coinbaseTX = Transaction.newCoinbaseTX(address, "");
            Block genesisBlock = Block.newGenesisBlock(coinbaseTX);
            lastBlockHash = genesisBlock.getHash();
            RocksDBUtils.getInstance().putBlock(genesisBlock);
            RocksDBUtils.getInstance().putLastBlockHash(lastBlockHash);
        }
        return new Blockchain(lastBlockHash);
    }

//    /**
//     * <p> 添加区块  </p>
//     *
//     * @param data
//     */
//    public void addBlock(String data) throws Exception {
//        String lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
//        if (StringUtils.isBlank(lastBlockHash)) {
//            throw new Exception("Fail to add block into blockchain ! ");
//        }
//        this.addBlock(Block.newBlock(lastBlockHash, data));
//    }

    /**
     * <p> 添加区块  </p>
     *
     * @param block
     */
    public void addBlock(Block block) throws Exception {
        RocksDBUtils.getInstance().putLastBlockHash(block.getHash());
        RocksDBUtils.getInstance().putBlock(block);
        this.lastBlockHash = block.getHash();
    }


    /**
     * 区块链迭代器
     */
    public class BlockchainIterator {

        private String currentBlockHash;

        public BlockchainIterator(String currentBlockHash) {
            this.currentBlockHash = currentBlockHash;
        }

        /**
         * 是否有下一个区块
         *
         * @return
         */
        public boolean hashNext() throws Exception {
            if (StringUtils.isBlank(currentBlockHash)) {
                return false;
            }
            Block lastBlock = RocksDBUtils.getInstance().getBlock(currentBlockHash);
            if (lastBlock == null) {
                return false;
            }
            // 创世区块直接放行
            if (lastBlock.getPrevBlockHash().length() == 0) {
                return true;
            }
            return RocksDBUtils.getInstance().getBlock(lastBlock.getPrevBlockHash()) != null;
        }


        /**
         * 返回区块
         *
         * @return
         */
        public Block next() throws Exception {
            Block currentBlock = RocksDBUtils.getInstance().getBlock(currentBlockHash);
            if (currentBlock != null) {
                this.currentBlockHash = currentBlock.getPrevBlockHash();
                return currentBlock;
            }
            return null;
        }
    }

    public BlockchainIterator getBlockchainIterator() {
        return new BlockchainIterator(lastBlockHash);
    }


    /**
     * 寻找包含未被花费的交易输出的交易
     *
     * @param address 钱包地址
     * @return
     */
    public Transaction[] findUnspentTransactions(String address) throws Exception {
        Map<String, int[]> allSpentTXOs = this.getAllSpentTXOs(address);
        Transaction[] unspentTx = {};

        // 再次遍历所有区块中的交易输出，如果

        for (BlockchainIterator blockchainIterator = this.getBlockchainIterator(); blockchainIterator.hashNext(); ) {
            Block block = blockchainIterator.next();
            for (Transaction transaction : block.getTransactions()) {

                String txId = Hex.encodeHexString(transaction.getTxId());

                int[] spendTXOs = allSpentTXOs.get(txId);

                for (int outIndex = 0; outIndex < transaction.getOutputs().length; outIndex++) {
                    if (spendTXOs != null && ArrayUtils.contains(spendTXOs, outIndex)) {
                        continue;
                    }

                    // 保存不存在 allSpentTXOs 中的交易输出
                    if (transaction.getOutputs()[outIndex].canBeUnlockedWith(address)) {
                        unspentTx = ArrayUtils.add(unspentTx, transaction);
                    }
                }
            }
        }
        return unspentTx;
    }


    /**
     * 从交易输入中查询区块链中所有已被花费了的交易输出
     *
     * @param address 钱包地址
     * @return 交易ID以及对应的交易输出下标地址
     * @throws Exception
     */
    private Map<String, int[]> getAllSpentTXOs(String address) throws Exception {
        // 定义TxId ——> spentOutIndex[]，存储交易ID与已被花费的交易输出数组索引值
        Map<String, int[]> spentTXOs = new HashMap<>();
        for (BlockchainIterator blockchainIterator = this.getBlockchainIterator(); blockchainIterator.hashNext(); ) {
            Block block = blockchainIterator.next();

            for (Transaction transaction : block.getTransactions()) {
                // 如果是 coinbase 交易，直接跳过，因为它不存在引用前一个区块的交易输出
                if (transaction.isCoinbase()) {
                    continue;
                }
                for (TXInput txInput : transaction.getInputs()) {
                    if (txInput.canUnlockOutputWith(address)) {
                        String inTxId = Hex.encodeHexString(txInput.getTxId());
                        int[] spentOutIndexArray = spentTXOs.get(inTxId);
                        if (spentOutIndexArray == null) {
                            spentTXOs.put(inTxId, new int[]{txInput.getTxOutputIndex()});
                        } else {
                            spentOutIndexArray = ArrayUtils.add(spentOutIndexArray, txInput.getTxOutputIndex());
                            spentTXOs.put(inTxId, spentOutIndexArray);
                        }
                    }
                }
            }
        }
        return spentTXOs;
    }


    /**
     * 查找钱包地址对应的UTXO
     *
     * @param address
     * @return
     */
    public TXOutput[] findUTXO(String address) throws Exception {
        Transaction[] txs = this.findUnspentTransactions(address);
        if (txs == null || txs.length == 0) {
            return new TXOutput[]{};
        }
        TXOutput[] result = {};
        for (Transaction tx : txs) {
            for (TXOutput txOutput : tx.getOutputs()) {
                if (txOutput.canBeUnlockedWith(address)) {
                    result = ArrayUtils.add(result, txOutput);
                }
            }
        }
        return result;
    }

}