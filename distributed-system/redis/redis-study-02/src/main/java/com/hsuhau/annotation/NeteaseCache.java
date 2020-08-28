package com.hsuhau.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface NeteaseCache {
    String value();

    String key();
}
