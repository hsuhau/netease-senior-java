package com.hsuhau.aop.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author hsuhau
 * @date 2020/7/23 0:13
 */
@Configuration
public class CacheConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    // 设置默认的超时时间
    private static final int EXPIRE = 3600;

    /**
     * 手动加载方式
     *
     * @return
     */
    @Bean("caffeineCache")
    public Cache cache() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                // 基于时间失效->写入之后开始计时失效
                .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
                .maximumSize(500)
                // 开启统计
                .recordStats()
                // 使用手动加载方式
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
                        System.out.println("缓存失效了 removed " + key + " cause " + cause.toString());
                        LOGGER.debug("缓存失效了 removed {} cause {}", key, cause);
                    }
                })
                .build();
        return cache;
    }

}
