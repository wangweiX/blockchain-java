package one.wangwei.blockchain.block;

import lombok.Getter;
import one.wangwei.blockchain.store.LevelDBUtils;
import org.apache.commons.lang3.StringUtils;

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
     * @return
     */
    public static Blockchain newBlockchain() {
        String lastBlockHash = LevelDBUtils.getInstance().getLastBlockHash();
        if (StringUtils.isBlank(lastBlockHash)) {
            Block genesisBlock = Block.newGenesisBlock();
            lastBlockHash = genesisBlock.getHash();
            LevelDBUtils.getInstance().putBlock(genesisBlock);
            LevelDBUtils.getInstance().putLastBlockHash(lastBlockHash);
        }
        return new Blockchain(lastBlockHash);
    }

    /**
     * <p> 添加区块  </p>
     *
     * @param data
     */
    public void addBlock(String data) throws Exception {
        String lastBlockHash = LevelDBUtils.getInstance().getLastBlockHash();
        if (StringUtils.isBlank(lastBlockHash)) {
            throw new Exception("Fail to add block into blockchain ! ");
        }
        this.addBlock(Block.newBlock(lastBlockHash, data));
    }

    /**
     * <p> 添加区块  </p>
     *
     * @param block
     */
    public void addBlock(Block block) {
        LevelDBUtils.getInstance().putLastBlockHash(block.getHash());
        LevelDBUtils.getInstance().putBlock(block);
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
        public boolean hashNext() {
            if (StringUtils.isBlank(currentBlockHash)) {
                return false;
            }
            Block lastBlock = LevelDBUtils.getInstance().getBlock(currentBlockHash);
            if (lastBlock == null) {
                return false;
            }
            // 创世区块直接放行
            if (lastBlock.getPrevBlockHash().length() == 0) {
                return true;
            }
            return LevelDBUtils.getInstance().getBlock(lastBlock.getPrevBlockHash()) != null;
        }


        /**
         * 返回区块
         *
         * @return
         */
        public Block next() {
            Block currentBlock = LevelDBUtils.getInstance().getBlock(currentBlockHash);
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

}
