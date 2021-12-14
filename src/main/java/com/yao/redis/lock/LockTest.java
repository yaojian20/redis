package com.yao.redis.lock;

import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yaojian on 2021/12/14 16:54
 *
 * @author
 */
public class LockTest {

    static  int count = 100;

    static  RedissonClient client = ClientUtil.getInstance();

    public static void main(String[] args) {

        lockTest1();
    }

    public static void lockTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i  = 0; i<100; i++){
            executorService.execute(() -> {
                RedisLock redisLock = new RedisLock();
                String id = UUID.randomUUID().toString();
                redisLock.lock(id);
                count--;
                System.out.println("count的值为：" + count);
                redisLock.unlock(id);
            });
        }
    }

    public static void lockTest1(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i  = 0; i<100; i++){
            executorService.execute(() -> {
                RLock lock = client.getLock("RLock");
                String id = UUID.randomUUID().toString();
                lock.lock();
                count--;
                System.out.println("count的值为：" + count);
                lock.unlock();
            });
        }
    }



}
