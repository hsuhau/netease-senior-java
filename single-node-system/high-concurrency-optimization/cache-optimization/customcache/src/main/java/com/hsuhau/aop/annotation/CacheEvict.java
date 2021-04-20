package com.hsuhau.aop.annotation;

/**
 * 缓存清除
 *
 * @author hsuhau
 * @date 2020/7/22 23:59
 */
public @interface CacheEvict {
    // 缓存名称
    String cacheName() default "";

    // 缓存 key
    String cacheKey();

    // 是否清空cacheName的全部数据
    boolean allEntries() default false;
}
