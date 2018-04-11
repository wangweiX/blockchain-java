package one.wangwei.blockchain.store;

import com.google.common.collect.Maps;
import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.util.SerializeUtils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

/**
 * 存储工具类
 *
 * @author wangwei
 * @date 2018/02/27
 */
public class LevelDBUtils {

    /**
     * 区块链数据文件
     */
    private static final String DB_FILE = "blockchain.db";
    /**
     * 区块桶前缀
     */
    private static final String BLOCKS_BUCKET_KEY = "blocks";
    /**
     * 最新一个区块
     */
    private static final String LAST_BLOCK_KEY = "l";

    private volatile static LevelDBUtils instance;

    public static LevelDBUtils getInstance() {
        if (instance == null) {
            synchronized (LevelDBUtils.class) {
                if (instance == null) {
                    instance = new LevelDBUtils();
                }
            }
        }
        return instance;
    }

    private DB db;

    /**
     * 区块桶
     */
    private Map<String, byte[]> blocksBucket;

    private LevelDBUtils() {
        openDB();
        initBlockBucket();
    }

    /**
     * 打开数据库
     */
    private void openDB() {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            db = factory.open(new File(DB_FILE), options);
        } catch (IOException e) {
            throw new RuntimeException("Can not open DB", e);
        }
    }


    /**
     * 初始化 blocks 数据桶
     */
    private void initBlockBucket() {
        byte[] blockBucketKey = SerializeUtils.serialize(BLOCKS_BUCKET_KEY);
        byte[] blockBucketBytes = db.get(blockBucketKey);
        if (blockBucketBytes != null) {
            blocksBucket = (Map) SerializeUtils.deserialize(blockBucketBytes);
        } else {
            blocksBucket = Maps.newHashMap();
            db.put(blockBucketKey, SerializeUtils.serialize(blocksBucket));
        }
    }

    /**
     * 保存最新一个区块的Hash值
     *
     * @param tipBlockHash
     */
    public void putLastBlockHash(String tipBlockHash) {
        blocksBucket.put(LAST_BLOCK_KEY, SerializeUtils.serialize(tipBlockHash));
        db.put(SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
    }

    /**
     * 查询最新一个区块的Hash值
     *
     * @return
     */
    public String getLastBlockHash() {
        byte[] lastBlockHashBytes = blocksBucket.get(LAST_BLOCK_KEY);
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
    public void putBlock(Block block) {
        blocksBucket.put(block.getHash(), SerializeUtils.serialize(block));
        db.put(SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
    }

    /**
     * 查询区块
     *
     * @param blockHash
     * @return
     */
    public Block getBlock(String blockHash) {
        return (Block) SerializeUtils.deserialize(blocksBucket.get(blockHash));
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        try {
            db.close();
        } catch (IOException e) {
            throw new RuntimeException("Fail to close db", e);
        }
    }
}
