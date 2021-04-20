package com.hsuhau.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 加载方式2-Callable
 * 所有类型的Guava Cache，不管有没有自动加载功能，都支持get(K, Callable<V>)方法。
 * 这个方法返回缓存中相应的值，或者用给定的Callable运算并把结果加入到缓存中。
 * 在整个加载方法完成前，缓存项相关的可观察状态都不会更改。
 * 这个方法简单地实现了模式”如果有缓存则返回；否则运算、缓存、然后返回“。
 * <p>
 * Guava Cache 缓存回收
 * <p>
 * 回收方式1-基于容量回收
 * maximumSize(long):当缓存中的元素数量超过指定值时。
 * <p>
 * 回收方式2-定时回收
 * expireAfterAccess(long, TimeUnit):缓存项在给定时间内没有被读/写访问，则回收
 * <p>
 * 回收方式3-基于引用回收 (Reference-based Eviction)
 * CacheBuilder.weakKeys():使用弱引用存储值。当key没有其他引用时，缓存项可以被垃圾回收。
 * CacheBuilder.weakValues():使用弱引用存储值。当value没有其他引用时，缓存项可以被垃圾回收。
 * CacheBuilder.softValues():使用软引用存储值，按照全局最近最少使用的顺序回收。
 *
 * @author hsuhau
 * @date 2020/7/22 14:53
 */
public class GuavaCacheDemo2 {
    static Cache<String, String> testCache = CacheBuilder.newBuilder()
            .maximumSize(3).build();


    public static void main(String[] args) {
        testCache.put("1234", "我是存在的");
        // 如果存在就获取，不存在返回null
        System.out.println(testCache.getIfPresent("key6"));
        try {
            // 获取key为123的缓存数据，如果有就返回，没有就返回call方法的返回值
            System.out.println(testCache.get("123", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "运算、缓存、然后返回";
                }
            }));

            // 获取key为123的缓存数据，如果有就返回，没有就返回call方法的返回值
            System.out.println(testCache.get("1234", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "我是打酱油的";
                }
            }));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
