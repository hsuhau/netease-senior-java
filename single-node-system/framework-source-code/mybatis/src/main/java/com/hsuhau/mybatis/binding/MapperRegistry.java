package com.hsuhau.mybatis.binding;

import com.hsuhau.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * MapperRegistry
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public class MapperRegistry {
    /**
     * the knownMappers
     */
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * 注册代理工厂
     *
     * @param type
     */
    public <T> void addMapper(Class<T> type) {
        this.knownMappers.put(type, new MapperProxyFactory<T>(type));
    }

    /**
     * 获取代理工厂实例
     *
     * @param type
     * @param sqlSession
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) this.knownMappers.get(type);

        return mapperProxyFactory.newInstance(sqlSession);
    }
}
