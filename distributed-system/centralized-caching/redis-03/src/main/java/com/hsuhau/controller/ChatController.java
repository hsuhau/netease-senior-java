package com.hsuhau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author hsuhau
 * @date 2020/8/29 01:46
 */
@RestController
public class ChatController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 返回土豪人数列表
     *
     * @param roomId
     * @return
     */
    @RequestMapping("/chat/roomInfo")
    public Object roomInfo(String roomId) {
        Long count = redisTemplate.opsForZSet().zCard("roominfo::" + roomId);
        System.out.println("房间号 = " + roomId + "，当前人数 = " + count);

        Set range = redisTemplate.opsForZSet().reverseRangeByScore("roominfo::" + roomId, 10, 5, 0, 2);
        for (Object o : range) {
            System.out.println(o.toString());
            
        }

        return "sum: " + count;
    }
}
