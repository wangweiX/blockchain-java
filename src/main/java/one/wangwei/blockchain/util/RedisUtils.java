package one.wangwei.blockchain.util;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * Redis工具类
 *
 * @author wangwei
 * @date 2018/02/06
 */
public class RedisUtils {

    private volatile static RedisUtils instance;

    private static Jedis jedis;

    private RedisUtils() {
        initRedis();
    }

    public static RedisUtils getInstance() {
        if (instance == null) {
            synchronized (RedisUtils.class) {
                if (instance == null) {
                    instance = new RedisUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化Redis客户端
     */
    private void initRedis() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("redis.properties"));

            JedisPoolConfig poolConfig = new JedisPoolConfig();

            poolConfig.setMinIdle(Integer.valueOf(properties.getProperty("redis.pool.minIdle")));
            poolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("redis.pool.maxIdle")));
            poolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("redis.pool.maxTotal")));
            poolConfig.setMinEvictableIdleTimeMillis(
                    Integer.valueOf(properties.getProperty("redis.pool.minEvictableIdleTimeMillis")));
            poolConfig.setTimeBetweenEvictionRunsMillis(
                    Integer.valueOf(properties.getProperty("redis.pool.timeBetweenEvictionRunsMillis")));
            poolConfig.setSoftMinEvictableIdleTimeMillis(
                    Integer.valueOf(properties.getProperty("redis.pool.softMinEvictableIdleTimeMillis")));
            poolConfig.setTestOnBorrow(Boolean.valueOf(properties.getProperty("redis.pool.testOnBorrow")));

            String redisHost = properties.getProperty("redis.pool.host");
            String hostPortStr = StringUtils.split(redisHost, "@")[1];
            String host = StringUtils.split(hostPortStr, ":")[0];
            String port = StringUtils.split(hostPortStr, ":")[1];

            String timeOut = properties.getProperty("redis.pool.timeout");

            JedisPool jedisPool = new JedisPool(poolConfig, host, Integer.valueOf(port), Integer.valueOf(timeOut));
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 存储数据
     *
     * @param key
     * @param value
     */
    public void putObject(Object key, Object value) {
        try {
            if (key == null) {
                return;
            }
            if (value == null) {
                return;
            }
            jedis.set(SerializeUtils.serialize(key), SerializeUtils.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询数据
     *
     * @param key
     */
    public Object getObject(Object key) {
        try {
            if (key == null) {
                return null;
            }
            byte[] dataBytes = jedis.get(SerializeUtils.serialize(key));
            return dataBytes == null ? null : SerializeUtils.deserialize(dataBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
