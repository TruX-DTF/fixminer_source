package edu.lu.uni.serval.utils;

import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Created by anilkoyuncu on 17/09/2018.
 */
public class PoolBuilder {
    public static JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    static final JedisPoolConfig poolConfig = buildPoolConfig();


    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1024);
        poolConfig.setMaxIdle(1024);
//        poolConfig.setMinIdle(16);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofMinutes(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofHours(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);


        return poolConfig;
    }
}
