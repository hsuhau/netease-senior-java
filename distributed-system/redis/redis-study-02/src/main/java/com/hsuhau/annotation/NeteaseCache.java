package com.hsuhau.annotation;

import java.lang.annotation.*;

/**
 * cache注解， 方法前，判断缓存，方法后，返回值，设置缓存
 */
@Documented
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface NeteaseCache {
    /**
     * 前缀
     *
     * @return
     */
    String value();

    /**
     * key的规则，可以使用springEL表达式，可以使用方法执行的一些参数
     *
     * @return
     */
    String key();
}
