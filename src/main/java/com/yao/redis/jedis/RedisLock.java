package com.yao.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaojian on 2022/2/11 16:02
 *
 * @author
 */
public class RedisLock {

    //获得锁
    public String getLock(String key,long timeOut){
        try {
            Jedis jedis = RedisManager.getJedis();
            String value = UUID.randomUUID().toString();
            long end = System.currentTimeMillis() + timeOut;
            while (System.currentTimeMillis() < end){
                //如果在超时时间内，让他一直尝试拿锁
                //key存在的情况下，不操作redis内存；也就是返回值是0
                if (jedis.setnx(key, value) == 1){
                    //说明设置成功，这个时候拿到了锁
                    jedis.expire(key,timeOut);
                    return value;
                }
                //检测key的过期时间，-1说明没有设置
                if (jedis.ttl(key) == -1){
                    jedis.expire(key,timeOut);
                }
                //睡1s
                TimeUnit.SECONDS.sleep(1);
            }

        } catch(Exception e){

        }


        return null;
    }

    public boolean releaseLock(String key, String value){
        //value是来判断是不是同一个锁
        try {
            Jedis jedis = RedisManager.getJedis();
            while (true){
                //用于监视一个(或多个) key ,如果在事务执行之前这个(或这些) key 被其他命令所改动,那么事务将被打断
                jedis.watch(key);
                //判断获得锁的线程跟当前释放锁的线程是不是同一个锁
                if (value.equals(jedis.get(key))){
                    Transaction transaction = jedis.multi();
                    transaction.del(key);
                    //因为redis事务事实上是好几个命令一起操作，所以会返回一个list
                    List<Object> list = transaction.exec();
                    if (list == null){
                        continue;
                    }
                    return true;

                }
                jedis.unwatch();
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


}
