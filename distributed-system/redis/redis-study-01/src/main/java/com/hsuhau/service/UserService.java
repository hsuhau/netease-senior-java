package com.hsuhau.service;

import com.hsuhau.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查看全部信息,redis缓存，用户信息以json字符串格式存在（序列化）
     *
     * @param userId
     * @return
     */
    public User findUserById(String userId) {
//        User user = null;
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            // 1.查询redis
//            String result = jedis.get(userId);
//            // 2.命中缓存
//            if (!StringUtils.isEmpty(result)) {
//                // 把缓存中的值，返回给用户
//                return JSONObject.parseObject(result, User.class);
//            }
//            String sql = "select * from t_user where uid=?";
//            user = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(User.class));
//            jedis.set(userId, JSONObject.toJSONString(user));
//            // 3.数据塞入redis中
//            return user;

        User user = new User();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 1.查询redis
            Map<String, String> map = jedis.hgetAll(userId);
            // 2.命中缓存
            if (!ObjectUtils.isEmpty(map)) {
                user.setAge(Integer.valueOf(map.get("age")));
                user.setImg(map.get("img"));
                user.setUid(map.get("uid"));
                user.setUname(map.get("uname"));
                // 把缓存中的值，返回给用户
                return user;
            }
            String sql = "select * from t_user where uid=?";
            user = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(User.class));
//            jedis.hset(userId, "uid", userId);
//            jedis.hset(userId, "uname", user.getUname());
//            jedis.hset(userId, "age", String.valueOf(user.getAge()));
//            jedis.hset(userId, "img", user.getImg());

            // 提高性能
            map.put("uid", userId);
            map.put("uname", user.getUname());
            map.put("img", user.getImg());
            map.put("age", String.valueOf(user.getAge()));
            jedis.hmset(userId, map);
            // 3.数据塞入redis中
            return user;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return user;
    }

    /**
     * 查看名称
     *
     * @param userId
     * @return
     */
    public String findUsernameById(String userId) {
        String uname = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 1.查询redis，单个属性
            if (jedis.hexists(userId, "uname")) {
                uname = jedis.hget(userId, "uname");
                return uname;
            }
            // 批量查询属性
//            List<String> hmget = jedis.hmget(userId, "uname", "img");
            // 2.命中缓存
//            if (!StringUtils.isEmpty(uname)) {
//                // 把缓存中的值，返回给用户
//                return uname;
//            }
            String sql = "select uname from t_user where uid=?";
            uname = jdbcTemplate.queryForObject(sql, new String[]{userId}, new BeanPropertyRowMapper<>(String.class));

            // 3.数据塞入redis，单独更新对象的一个属性
            jedis.hset(userId, "uname", uname);
            return uname;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return uname;
    }

}