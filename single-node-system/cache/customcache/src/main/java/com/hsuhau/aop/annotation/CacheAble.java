package com.hsuhau.aop.annotation;

/**
 * 缓存读取
 *
 * @author hsuhau
 * @date 2020/7/22 23:59
 */
public @interface CacheAble {
    // 缓存名称
    String cacheName() default "";

    // 缓存Key
    String cacheKey();

    // 有效时间（单位：秒），默认1小时
    int expire() default 3600;

    // 缓存主动刷新时间（单位：秒），默认-1表示不主动刷新
    int refresh() default -1;
}
