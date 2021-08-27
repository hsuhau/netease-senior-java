package com.hsuhau.service;

import com.hsuhau.annotation.NeteaseCache;
import com.hsuhau.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查看全部信息
     *
     * @param userId
     * @return
     */
    public User findUserById(String userId) {

        User user = new User();
        try {
            // 1.查询redis缓存
            Object result = redisTemplate.opsForValue().get(userId);
            if (!ObjectUtils.isEmpty(result)) {
                return ((User) result);
            }
            // 2.查询数据库
            String sql = "select * from t_user where uid=?";
            user = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(User.class));
            // 3.设置缓存
            redisTemplate.opsForValue().set(userId, user);
            return user;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return user;
    }

    /**
     * 查看全部信息
     *
     * @param userId
     * @return
     */
    // key = "#userId" key的生成规则
//    @Cacheable(cacheManager = "cacheManager", value = "user", key = "#userId")
    @NeteaseCache( value = "user", key = "#userId")
    public User findUserByIdAnnotation(String userId) {
        System.out.println("使用了注解实现");
        String sql = "select * from t_user where uid=?";
        User user = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(User.class));
        return user;
    }

    /**
     * 更新用户信息，方法执行结束，清理缓存
     *
     * @param user
     */
    @CacheEvict(value = "user", key = "#user.uid")
    public void updateUserByIdAnnotation(User user) {
        String sql = "update t_user set uname = ? and img = ? and age = ? where uid = ?";
        jdbcTemplate.update(sql, new String[]{user.getUname(), user.getImg(), String.valueOf(user.getAge()), user.getUid()});
    }
}