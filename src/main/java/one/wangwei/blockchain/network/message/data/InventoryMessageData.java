package one.wangwei.blockchain.network.message.data;

import com.google.common.collect.Lists;
import lombok.Data;
import one.wangwei.blockchain.network.message.InvTypeEnum;
import org.apache.commons.codec.binary.Hex;

import java.util.List;

/**
 * 库存清单消息
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Data
public class InventoryMessageData extends BaseMessageData {

    /**
     * 库存清单类型("tx","block")
     */
    private InvTypeEnum invType;
    /**
     * 区块Hash列表
     */
    private List<String> blockHashes = Lists.newArrayList();
    /**
     * 交易Hash列表
     */
    private List<String> txHashes = Lists.newArrayList();

    /**
     * 新增TxID
     *
     * @param txIdHash
     */
    public void addTxIdHash(String txIdHash) {
        txHashes.add(txIdHash);
    }

    /**
     * 新增TxID
     *
     * @param txIdBytes
     */
    public void addTxIdHash(byte[] txIdBytes) {
        this.addTxIdHash(Hex.encodeHexString(txIdBytes));
    }

    /**
     * 添加区块Hash
     *
     * @param blockHash
     */
    public void addBlockHash(String blockHash) {
        blockHashes.add(blockHash);
    }

}
