package one.wangwei.blockchain.transaction;

import one.wangwei.blockchain.block.Blockchain;

/**
 * 未被花费的交易输出池
 *
 * @author wangwei
 * @date 2018/03/31
 */
public class UTXOSet {

    private Blockchain blockchain;

    // 初始化，从DB中加载索引

    /**
     * 重建 UTXO 池索引
     */
    public static void reIndex() throws Exception {
        Blockchain blockchain = Blockchain.initBlockchainFromDB();

    }

    // 更新 UTXO 池

    // 查找 UTXO

    // 查找用于支付的 UTXO

    //

}
