package com.hsuhau.mybatis.executor;

import com.hsuhau.mybatis.mapping.MappedStatement;

import java.util.List;

/**
 * mybatis执行器
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public interface Executor {

    /**
     * 查询数据库
     *
     * @param ms
     * @param parameter
     * @return
     * @see
     */
    <E> List<E> doQuery(MappedStatement ms, Object parameter);

    /**
     * 更新操作
     *
     * @param ms
     * @param parameter
     */
    void doUpdate(MappedStatement ms, Object parameter);
}
