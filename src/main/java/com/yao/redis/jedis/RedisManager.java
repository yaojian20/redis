package com.yao.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by yaojian on 2022/2/11 15:53
 *
 * @author
 */
public class RedisManager {


    //redis连接池
    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //设置最大线程数
        jedisPoolConfig.setMaxTotal(20);
        //设置最大活跃线程数
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig, "139.224.162.184", 6379 );
    }

    //redis连接
    public static Jedis getJedis() throws Exception {
        if (null != jedisPool){
            return jedisPool.getResource();
        }
        throw new Exception("jedis pool 实例为空");

    }




}
