package com.hsuhau.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.hsuhau.bean.User;

import java.sql.*;

/**
 * @author hsuhau
 */
public class JDBCTest {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立数据库连接serverTimezone
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "waibibabo");
            // sql语句
            String sql = "select * from user where name = ?";
            // 预处理statement
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数，针对sql中的占位符中
            preparedStatement.setString(1, "hsuhau");
            // 发起请求
            resultSet = preparedStatement.executeQuery();
            User user = new User();

            // 遍历查询结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                // 封装结果为User对象
                user.setId(id);
                user.setName(name);
                user.setAge(age);
                user.setGender(gender);
            }
            System.out.println("JSONObject.toJSONString(user) = " + JSONObject.toJSONString(user));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}