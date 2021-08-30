package com.hsuhau.mybatis;

import com.alibaba.fastjson.JSONObject;
import com.hsuhau.common.entity.User;
import com.hsuhau.mybatis.dao.UserMapper;
import com.hsuhau.mybatis.session.SqlSession;
import com.hsuhau.mybatis.session.SqlSessionFactory;
import com.hsuhau.mybatis.session.SqlSessionFactoryBuilder;

// mybatis测试类
public class MybatisTest {
    public static void main(String[] args) {

        // 第一步，读取conf.properties配置文件，构建SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build("conf.properties");

        // 第二步，打开SqlSession
        SqlSession sqlSession = factory.openSession();

        // 第三步，获取Mapper接口对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 第四步，调用Mapper接口对象的方法操作数据库
        User user = userMapper.getUser(1);

        // 第五步，业务处理
        System.out.println("JSONObject.toJSONString(user) = " + JSONObject.toJSONString(user));
    }
}
