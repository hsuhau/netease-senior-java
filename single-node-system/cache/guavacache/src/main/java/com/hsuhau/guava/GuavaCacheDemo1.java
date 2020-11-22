package com.hsuhau.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.util.concurrent.TimeUnit;

/**
 * 加载方式1:CacheLoader
 * 1、设置缓存容量
 * 2、设置超时时间
 * 3、提供移除监听器
 * 4、提供缓存加载器
 * 5、构建缓存
 *
 * @author hsuhau
 * @date 2020/7/22 2:26
 */
public class GuavaCacheDemo1 {
    public static void main(String[] args) {
        // 提供缓存加载器
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                Thread.sleep(1000);
                if ("key".equals(key)) {
                    return null;
                }
                System.out.println(key + "is loaded from a cacheLoader!");
                return key + "'s value";
            }
        };

        RemovalListener<String, String> removalListener = removalNotification -> {
            System.out.println("[" + removalNotification.getKey() + ":" + removalNotification.getValue() + "] is evicted!");
        };

        LoadingCache<String, String> testCache = CacheBuilder.newBuilder()
                // 设置缓存容量
                .maximumSize(5)
                // 设置超时时间
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 提供移除监听器
                .removalListener(removalListener)
                // 提供缓存加载器loader：构建缓存
                .build(loader);

        // 由于缓存的容器只设置了5个，存入10个就会由guava基于容量回收掉5个
        for (int i = 0; i < 10; i++) {
            String key = "key" + i;
            String value = "value" + i;
            testCache.put(key, value);
            System.out.println("[" + key + ":" + value + "] is put into cache!");
        }

        // 如果存在就获取
        System.out.println(testCache.getIfPresent("key6"));

        try {
            System.out.println(testCache.get("key"));
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("不存在的key，会报错");
        }

    }
}
