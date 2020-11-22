package com.hsuhau.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine 手动加载
 *
 * @author hsuhau
 * @date 2020/7/22 20:53
 */
public class CaffeineManualLoad {
    public static void main(String[] args) throws Exception {
        Cache<String, Object> cache = Caffeine.newBuilder()
                // 基于时间失效->写入之后开始计时失效
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                .maximumSize(10_000)
                .build();

        // 返回 key + 当前时间作为 value
        Function<Object, Object> getFunc = key -> key + "_" + System.currentTimeMillis();

        String key1 = "key1";

        // 获取 key1 对应的值，如果获取不到则执行 getFunc
        Object value = cache.get(key1, getFunc);
        System.out.println("value = " + value);

        // 让缓存到期
        Thread.sleep(2001);

        value = cache.getIfPresent(key1);
        System.out.println("value = " + value);

        // 设置 key1 的值
        cache.put(key1, "putValue");
        value = cache.get(key1, getFunc);
        System.out.println("value = " + value);

        ConcurrentMap<String, Object> asMap = cache.asMap();
        System.out.println("asMap = " + asMap);

        // 删除 key1
        cache.invalidate(key1);
        asMap = cache.asMap();
        System.out.println("asMap = " + asMap);
    }
}
