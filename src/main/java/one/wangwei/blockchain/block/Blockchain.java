package one.wangwei.blockchain.block;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * <p> 区块链 </p>
 *
 * @author wangwei
 * @date 2018/02/02
 */
public class Blockchain {

    @Getter
    private List<Block> blockList;

    public Blockchain(List<Block> blockList) {
        this.blockList = blockList;
    }

    /**
     * <p> 创建区块链 </p>
     *
     * @return
     */
    public static Blockchain newBlockchain() {
        List<Block> blocks = new LinkedList<>();
        blocks.add(Block.newGenesisBlock());
        return new Blockchain(blocks);
    }

    /**
     * <p> 添加区块  </p>
     *
     * @param data
     */
    public void addBlock(String data) {
        Block previousBlock = blockList.get(blockList.size() - 1);
        this.addBlock(Block.newBlock(previousBlock.getHash(), data));
    }

    /**
     * <p> 添加区块  </p>
     *
     * @param block
     */
    public void addBlock(Block block) {
        this.blockList.add(block);
    }

}
