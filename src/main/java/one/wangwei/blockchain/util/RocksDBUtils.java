package one.wangwei.blockchain.util;

import com.google.common.collect.Maps;
import one.wangwei.blockchain.block.Block;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.nio.ByteBuffer;
import java.util.Map;

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
    private static final String BLOCKS_BUCKET_KEY = "blocks";

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

    private RocksDB rocksDB;

    /**
     * 区块桶
     */
    private Map<ByteBuffer, byte[]> blocksBucket;

    private RocksDBUtils() {
        initRocksDB();
        initBlockBucket();
    }

    /**
     * 初始化RocksDB
     */
    private void initRocksDB() {
        try {
            Options options = new Options();
            options.setCreateIfMissing(true);
            rocksDB = RocksDB.open(options, DB_FILE);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化 blocks 数据桶
     */
    private void initBlockBucket() {
        try {
            byte[] blockBucketKey = SerializeUtils.serialize(BLOCKS_BUCKET_KEY);
            byte[] blockBucketBytes = rocksDB.get(blockBucketKey);
            if (blockBucketBytes != null) {
                blocksBucket = (Map) SerializeUtils.deserialize(blockBucketBytes);
            } else {
                blocksBucket = Maps.newHashMap();
                rocksDB.put(blockBucketKey, SerializeUtils.serialize(blocksBucket));
            }
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
        blocksBucket.put(ByteBuffer.wrap(SerializeUtils.serialize("l")),
                SerializeUtils.serialize(tipBlockHash));
        rocksDB.put(SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
    }

    /**
     * 查询最新一个区块的Hash值
     *
     * @return
     */
    public String getLastBlockHash() {
        byte[] lastBlockHashBytes = blocksBucket.get(ByteBuffer.wrap(SerializeUtils.serialize("l")));
        if (lastBlockHashBytes != null) {
            return (String) SerializeUtils.deserialize(lastBlockHashBytes);
        }
        return "";
    }

    /**
     * 保存区块
     *
     * @param block
     */
    public void putBlock(Block block) throws Exception {
        byte[] key = SerializeUtils.serialize(block.getHash());
        blocksBucket.put(ByteBuffer.wrap(key), SerializeUtils.serialize(block));
        rocksDB.put(SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
    }

    /**
     * 查询区块
     *
     * @param blockHash
     * @return
     */
    public Block getBlock(String blockHash) {
        byte[] key = SerializeUtils.serialize(blockHash);
        return (Block) SerializeUtils.deserialize(blocksBucket.get(ByteBuffer.wrap(key)));
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (rocksDB != null) {
            rocksDB.close();
        }
    }
}
