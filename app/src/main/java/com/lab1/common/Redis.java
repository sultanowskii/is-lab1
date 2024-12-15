package com.lab1.common;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

@Component
public class Redis {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    private JedisPool pool;

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(200);
        poolConfig.setMinIdle(20);
        // Maximum wait time when the connections are used up
        poolConfig.setMaxWait(Duration.ofMillis(3000));
        // When a connection is returned, a check will be performed first. Once the check fails, the connection will be terminated.
        poolConfig.setTestOnReturn(false);
        return poolConfig;
    }

    private JedisPool getPool() {
        return new JedisPool(buildPoolConfig(), host, port, 2000);
    }

    public Jedis getResource() {
        if (pool == null) {
            pool = getPool();
        }

        Jedis jedis;
        try {
            jedis = pool.getResource();
            jedis.ping();
        } catch (JedisException e) {
            pool.destroy();
            pool = getPool();
            jedis = pool.getResource();
        }

        return jedis;
    }
}
