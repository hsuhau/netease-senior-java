package com.hsuhau.mybatis.session;

/**
 * SqlSessionFactory 工厂模式
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public interface SqlSessionFactory {

    /**
     * 开启数据库会话
     *
     * @return
     * @see
     */
    SqlSession openSession();

}
