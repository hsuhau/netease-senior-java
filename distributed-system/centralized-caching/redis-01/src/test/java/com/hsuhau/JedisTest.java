package com.hsuhau;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTest {
    @Test
    public void test() {
        // java 客户端示例，jedis初学者友好，操作与控制台类似
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String result = jedis.get("hello");
        System.out.print(result);

        // pool
        // JedisPool jedisPool = new JedisPool();
    }
}
