package com.hsuhau.map;


import org.junit.Test;

public class CacheProviderTest {

    @Test
    public void test() throws Exception {
        CacheProvider cache = new CacheProvider();

        String key = "id";

        // 不设置过期时间
        System.out.println("********** 不设置过期时间时间 **********");
        cache.put(key, 123);
        System.out.println("key = " + key + " value = " + cache.get(key));
        System.out.println("key = " + key + " value = " + cache.remove(key));
        System.out.println("key = " + key + " value = " + cache.get(key));

        // 设置过期时间
        System.out.println("************ 设置过期时间 **************");
        cache.put(key, 123456, 1000L);
        System.out.println("key = " + key + " value = " + cache.get(key));
        Thread.sleep(2000);
        System.out.println("休眠两秒");
        System.out.println("key = " + key + " value = " + cache.get(key));
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
