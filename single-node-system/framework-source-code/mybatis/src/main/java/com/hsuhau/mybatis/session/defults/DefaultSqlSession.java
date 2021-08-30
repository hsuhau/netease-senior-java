package com.hsuhau.mybatis.session.defults;

import com.hsuhau.mybatis.executor.Executor;
import com.hsuhau.mybatis.executor.SimpleExecutor;
import com.hsuhau.mybatis.mapping.MappedStatement;
import com.hsuhau.mybatis.session.Configuration;
import com.hsuhau.mybatis.session.SqlSession;
import com.hsuhau.mybatis.utils.CommonUtis;

import java.util.List;

/**
 * 默认SQL会话实现类
 *
 * @Author 网易云课堂微专业 - java高级开发工程
 **/
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    private final Executor executor;

    /**
     * 默认构造方法
     *
     * @param configuration
     */
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = new SimpleExecutor(configuration);
    }

    /**
     * 查询带条记录
     */
    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        List<T> results = this.<T>selectList(statementId, parameter);

        return CommonUtis.isNotEmpty(results) ? results.get(0) : null;
    }

    /**
     * 查询多条记录
     *
     * @param statementId ID为mapper类全名+方法名
     * @param parameter   参数列表
     * @return
     */
    @Override
    public <E> List<E> selectList(String statementId, Object parameter) {
        MappedStatement mappedStatement = this.configuration.getMappedStatement(statementId);
        return this.executor.<E>doQuery(mappedStatement, parameter);
    }

    /**
     * 更新
     *
     * @param statementId
     * @param parameter
     */
    @Override
    public void update(String statementId, Object parameter) {
        MappedStatement mappedStatement = this.configuration.getMappedStatement(statementId);
        this.executor.doUpdate(mappedStatement, parameter);
    }

    @Override
    public void insert(String statementId, Object parameter) {
        //TODO 待实现
    }

    /**
     * 获取Mapper
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.<T>getMapper(type, this);
    }

    /**
     * getConfiguration
     *
     * @return
     */
    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

}
