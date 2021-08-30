package com.hsuhau.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    /**
     * 创建对象，Spring托管
     *
     * @return
     */
    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);

        return jedisPool;
    }
}