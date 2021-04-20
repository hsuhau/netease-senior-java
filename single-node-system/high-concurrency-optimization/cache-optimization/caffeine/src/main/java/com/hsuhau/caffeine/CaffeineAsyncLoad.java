package com.hsuhau.caffeine;

/**
 * Caffeine 异步加载
 *
 * @author hsuhau
 * @date 2020/7/22 21:09
 */
public class CaffeineAsyncLoad {
    public static void main(String[] args) {
        /*LoadingCache<Object, Object> loadingCache = Caffeine.newBuilder()
                // 基于时间失效->写入之后开始计时失效
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                // 同步加载和手动加载的区别就是在构建缓存时提供一个同步的加载方法
                .build(key -> createTestValue(key));*/
    }
}
