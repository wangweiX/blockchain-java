package one.wangwei.blockchain.util;

import one.wangwei.blockchain.block.Block;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * RocksDB 工具类
 *
 * @author wangwei
 * @date 2018/02/27
 */
public class RocksDBUtils {

    /**
     * 区块链数据文件
     */
    private static final String DB_FILE = "blockchain.db";
    /**
     * 区块桶前缀
     */
    private static final String BLOCKS_BUCKET_PREFIX = "blocks_";

    private volatile static RocksDBUtils instance;

    public static RocksDBUtils getInstance() {
        if (instance == null) {
            synchronized (RocksDBUtils.class) {
                if (instance == null) {
                    instance = new RocksDBUtils();
                }
            }
        }
        return instance;
    }

    private static RocksDB rocksDB;

    private RocksDBUtils() {
        initRocksDB();
    }

    /**
     * 初始化RocksDB
     */
    private void initRocksDB() {
        try {
            rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), DB_FILE);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存最新一个区块的Hash值
     *
     * @param tipBlockHash
     */
    public void putLastBlockHash(String tipBlockHash) throws Exception {
        rocksDB.put((BLOCKS_BUCKET_PREFIX + "l").getBytes(), tipBlockHash.getBytes());
    }

    /**
     * 查询最新一个区块的Hash值
     *
     * @return
     */
    public String getLastBlockHash() throws Exception {
        byte[] lastBlockHashBytes = rocksDB.get((BLOCKS_BUCKET_PREFIX + "l").getBytes());
        if (lastBlockHashBytes != null) {
            return new String(lastBlockHashBytes);
        }
        return "";
    }

    /**
     * 保存区块
     *
     * @param block
     */
    public void putBlock(Block block) throws Exception {
        rocksDB.put((BLOCKS_BUCKET_PREFIX + block.getHash()).getBytes(), SerializeUtils.serialize(block));
    }

    /**
     * 查询区块
     *
     * @param blockHash
     * @return
     */
    public Block getBlock(String blockHash) throws Exception {
        return (Block) SerializeUtils.deserialize(rocksDB.get((BLOCKS_BUCKET_PREFIX + blockHash).getBytes()));
    }

}
