package com.hsuhau.mybatis.executor.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * StatementHandler负责处理Mybatis与JDBC之间Statement的交互
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public interface StatementHandler {

    /**
     * SQL预处理
     *
     * @param paramConnection
     * @return
     * @throws SQLException
     * @see
     */
    PreparedStatement prepare(Connection paramConnection) throws SQLException;

    /**
     * 查询数据库
     *
     * @param preparedStatement
     * @return
     * @throws SQLException
     * @see
     */
    ResultSet query(PreparedStatement preparedStatement) throws SQLException;

    /**
     * update
     *
     * @param preparedStatement
     * @throws SQLException
     */
    void update(PreparedStatement preparedStatement) throws SQLException;
}
