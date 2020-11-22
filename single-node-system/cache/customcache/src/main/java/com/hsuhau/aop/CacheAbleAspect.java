package com.hsuhau.aop;

import com.github.benmanes.caffeine.cache.Cache;
import com.hsuhau.aop.annotation.CacheAble;
import com.hsuhau.aop.annotation.CacheEvict;
import com.hsuhau.aop.annotation.CachePut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

/**
 * 自定义缓存注解AOP实现
 *
 * @author hsuhau
 * @date 2020/7/23 0:28
 */

@Aspect
@Component
public class CacheAbleAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheAbleAspect.class);

    @Autowired
    private Cache caffeineCache;

    @Autowired
    private DefaultKeyGenerator keyGenerator;

    /**
     * 读取缓存数据
     * 定义增强，pointcut连接点使用@annotation(xxx)进行定义
     *
     * @param point
     * @param cacheAble
     * @return
     * @throws Throwable
     */
    // cacheAble 与 下面参数名称 cacheAble 对应
    @Around(value = "@annotation(cacheAble)")
    public Object cached(final ProceedingJoinPoint point, CacheAble cacheAble) throws Throwable {
        try {
            Object valueData = null;
            // 生成缓存key
//            String key = cacheAble.cacheKey();
            String key = keyGenerator.generateKey(point, cacheAble.cacheName(), cacheAble.cacheKey());
            byte[] value = ((byte[]) caffeineCache.getIfPresent(key));
            if (value != null) {
                // 如果缓存有值，需要判断缓存设置和当前缓存的失效时间
                int refresh = cacheAble.refresh();
                if (refresh > 0) {

                }
                return SerializationUtils.deserialize(value);
            }
            SerializationUtils.serialize(value);
            if (valueData == null) {
                valueData = point.proceed();
            }

            if (cacheAble.expire() > 0) {
                caffeineCache.put(key, SerializationUtils.serialize(valueData));
            } else {
                caffeineCache.put(key, SerializationUtils.serialize(valueData));
            }

            return valueData;
        } catch (Exception e) {
            LOGGER.error("读取缓存失败，异常息：" + e.getMessage());
            return point.proceed();
        }
    }

    /**
     * 新增缓存
     *
     * @param point
     * @param cachePut
     * @return
     * @throws Throwable
     */
    // cachePut 与 下面参数名称 cachePut 对应
    @Around(value = "@annotation(cachePut)")
    public Object cachePut(final ProceedingJoinPoint point, CachePut cachePut) throws Throwable {
        try {
            Object valueData = point.proceed();
            // 生成缓存 key
            //            String key = cacheAble.cacheKey();
            String key = keyGenerator.generateKey(point, cachePut.cacheName(), cachePut.cacheKey());
            // 写入缓存
            if (cachePut.expire() > 0) {
                caffeineCache.put(key, SerializationUtils.serialize(valueData));
            } else {
                caffeineCache.put(key, SerializationUtils.serialize(valueData));
            }
            return valueData;
        } catch (Exception e) {
            LOGGER.error("写入缓存失败，异常信息：" + e.getMessage());
            return point.proceed();
        }
    }

    /**
     * 删除缓存
     *
     * @param point
     * @param cacheEvict
     * @throws Throwable
     */
    // cacheEvict 与 下面参数名称 cacheEvict 对应
    @Around(value = "@annotation(cacheEvict)")
    public Object cacheEvict(final ProceedingJoinPoint point, CacheEvict cacheEvict) throws Throwable {
        try {
            String cacheName = cacheEvict.cacheName();
            boolean allEntries = cacheEvict.allEntries();
            if (allEntries) {
                if (cacheName == null) {
                    Signature signature = point.getSignature();
                    cacheName = signature.getDeclaringTypeName() + "." + signature.getName();
                }
                caffeineCache.invalidate("CacheName_" + cacheName);
            } else {
                //            String key = cacheAble.cacheKey();
                String key = keyGenerator.generateKey(point, cacheEvict.cacheName(), cacheEvict.cacheKey());
                caffeineCache.invalidate(key);
            }
        } catch (Exception e) {
            LOGGER.error("删除缓存失败，异常信息：" + e.getMessage());
        }
        return point.proceed();
    }
}
