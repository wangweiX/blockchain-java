package one.wangwei.blockchain;

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
    private final List<Block> blockList = new LinkedList<>();

    /**
     * <p> 创建区块链 </p>
     *
     * @return
     */
    public static Blockchain newBlockchain() {
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock(Block.newGenesisBlock());
        return blockchain;
    }

    /**
     * <p> 添加区块  </p>
     *
     * @param data
     */
    public void addBlock(String data) {
        Block previousBlock = blockList.get(blockList.size() - 1);
        this.addBlock(new Block(previousBlock.getHash(), data));
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
