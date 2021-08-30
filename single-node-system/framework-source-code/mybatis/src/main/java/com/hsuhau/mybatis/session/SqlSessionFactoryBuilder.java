package com.hsuhau.mybatis.session;

import com.hsuhau.mybatis.session.defults.DefaultSqlSessionFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * SqlSessionFactoryBuilder - 构建者模式
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public class SqlSessionFactoryBuilder {

    /**
     * build
     *
     * @param fileName
     * @return
     * @see
     */
    public SqlSessionFactory build(String fileName) {

        InputStream inputStream = SqlSessionFactoryBuilder.class.getClassLoader().getResourceAsStream(fileName);

        return build(inputStream);
    }

    /**
     * build
     *
     * @param inputStream
     * @return
     * @see
     */
    public SqlSessionFactory build(InputStream inputStream) {
        try {
            Configuration.PROPS.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultSqlSessionFactory(new Configuration());
    }
}
