package com.hsuhau.mybatis.executor.parameter;

import java.sql.PreparedStatement;

/**
 * ParameterHandler 参数处理
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public interface ParameterHandler {

    /**
     * 设置参数
     *
     * @param paramPreparedStatement
     * @see
     */
    void setParameters(PreparedStatement paramPreparedStatement);
}
