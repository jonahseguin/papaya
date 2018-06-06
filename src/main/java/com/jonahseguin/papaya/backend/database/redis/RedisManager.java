package com.jonahseguin.papaya.backend.database.redis;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.annotations.SerializeData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Created by jonahseguin on 2017-08-12.
 */
public class RedisManager {

    private final JedisPool jedisPool;

    @SerializeData("database.redis.host")
    private String redisHost = "localhost";

    @SerializeData("database.redis.port")
    private int redisPort = 6379;

    @SerializeData("database.redis.use-auth")
    private boolean useRedisAuth = false;

    @SerializeData("database.redis.password")
    private String redisPassword = "password";

    public RedisManager() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort);
    }

    public Jedis getJedisResource() {
        try {
            Jedis jedis = jedisPool.getResource();
            if (useRedisAuth) {
                jedis.auth(redisPassword);
            }
            return jedis;
        } catch (JedisException ex) {
            Papaya.getInstance().getErrorHandler().addRecord(ex, "Error while getting Jedis resource");
            return null;
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void returnJedisResource(Jedis jedis) {
        jedis.close();
    }


}
