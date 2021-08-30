package com.hsuhau.mybatis.executor.resultset;

import java.sql.ResultSet;
import java.util.List;

/**
 * 结果集处理
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public interface ResultSetHandler {

    /**
     * 处理查询结果
     *
     * @param resultSet
     * @return
     * @see
     */
    <E> List<E> handleResultSets(ResultSet resultSet);

}
