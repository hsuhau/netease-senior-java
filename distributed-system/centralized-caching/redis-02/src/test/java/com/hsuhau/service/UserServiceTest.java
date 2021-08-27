package com.hsuhau.service;

import com.alibaba.fastjson.JSONObject;
import com.hsuhau.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void findUserById() {
        User u001 = userService.findUserById("u001");
        // RedisTemplate 默认序列化方式JdkSerializationRedisSerializer，不可读
        // "\xac\xed\x00\x05t\x00\x04u001"
        // "\xac\xed\x00\x05sr\x00\x14com.hsuhau.pojo.User\x1e:\x9b\xa00y'X\x02\x00\x04L\x00\x03aget\x00\x13Ljava/lang/Integer;L\x00\x03imgt\x00\x12Ljava/lang/String;L\x00\x03uidq\x00~\x00\x02L\x00\x05unameq\x00~\x00\x02xpsr\x00\x11java.lang.Integer\x12\xe2\xa0\xa4\xf7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x17t\x001https://telegra.ph/file/7ca6fb8f7d0275cd20cab.jpgt\x00\x04u001t\x00\x04jean"
        System.out.println(JSONObject.toJSON(u001));
    }

    @Test
    public void findUserByIdAnnotation() {
        User u002 = userService.findUserByIdAnnotation("u002");
        System.out.println(JSONObject.toJSON(u002));
    }

    @Test
    public void updateUserByIdAnnotation() {
        User user = new User("u003", "Hokunaimeko", 20, "https://telegra.ph/file/e83d60ab9f1168002ba4c.jpg");
        userService.updateUserByIdAnnotation(user);
    }
}