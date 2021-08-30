package com.hsuhau.map;


import org.junit.Test;

import java.util.UUID;

public class FIFOCacheProviderTest {

    @Test
    public void test() throws Exception {
        FIFOCacheProvider cache = new FIFOCacheProvider(10);
        for (int i = 0; i < 15; i++) {
            cache.put("id" + i, UUID.randomUUID());
        }
        System.out.println("缓存的大小：" + cache.size());
        System.out.println("缓存数据：\n" + cache);
    }
}
