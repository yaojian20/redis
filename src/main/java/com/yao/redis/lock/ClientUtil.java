package com.yao.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Created by yaojian on 2021/12/14 17:05
 *
 * @author
 */
public class ClientUtil {

    public static RedissonClient getInstance(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://139.224.162.184:6379");
        RedissonClient client = Redisson.create(config);
        //config.useSingleServer().setPassword("redis1234");
        return client;
    }


}
