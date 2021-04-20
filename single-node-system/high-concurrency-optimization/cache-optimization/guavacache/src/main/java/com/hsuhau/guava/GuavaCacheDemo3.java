package com.hsuhau.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author hsuhau
 * @date 2020/7/22 15:14
 */
public class GuavaCacheDemo3 {
    static Cache<String, Object> testCache = CacheBuilder.newBuilder()
            // 当值没有其他（强或软）引用时，缓存项可以被垃圾回收
            .weakValues()
            // 开启Guava Cache的统计功能
            .recordStats()
            .build();

    public static void main(String[] args) {
        Object obj1 = new Object();
        testCache.put("1234", obj1);
        obj1 = "123";
        // 主动GC
        System.gc();
        System.out.println(testCache.getIfPresent("1234"));

        /*
         * stats()方法会返回CacheStats 对象以提供以下统计信息：
         * hitRate():缓存命中率；
         * averageLoadPenalty(): 加载心智的平均时间，单位为纳秒；
         * evictionCount(): 缓存项被回收的总数，不包括显式清除。
         */
        System.out.println(testCache.stats());
    }
}

