package com.hsuhau;

import com.alibaba.fastjson.JSONObject;
import com.hsuhau.pojo.User;
import com.hsuhau.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private UserService userService;

    @Test
    public void test() {
        String userId = "hello";
        User user = userService.findUserById(userId);
        System.out.println(JSONObject.toJSON(user));
    }
}