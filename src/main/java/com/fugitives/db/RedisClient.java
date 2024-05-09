package com.fugitives.db;

import lombok.NonNull;
import redis.clients.jedis.Jedis;

public class RedisClient {

    private static RedisClient redisClient;
    private final Jedis jedis;

    private RedisClient(){
        jedis = new Jedis("redis://redis:6379");
    }

    public static RedisClient getInstance(){
        if (redisClient == null){
            redisClient = new RedisClient();
        }
        return redisClient;
    }

    public void saveObject(@NonNull Object key, Object value){
        jedis.set(key.toString(),value.toString());
    }

    public String getObject(@NonNull Object key){
        return jedis.get(key.toString());
    }

    public void close(){
        jedis.close();
    }
}
