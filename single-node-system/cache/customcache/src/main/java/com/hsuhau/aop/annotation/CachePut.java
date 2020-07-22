package com.hsuhau.aop.annotation;

/**
 * 缓存写入
 *
 * @author hsuhau
 * @date 2020/7/23 0:07
 */
public @interface CachePut {
    // 缓存名称
    String cacheName() default "";

    // 缓存Key
    String cacheKey();

    // 有效时间（单位：秒），默认1小时
    int expire() default 3600;
}
