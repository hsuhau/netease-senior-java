package com.hsuhau.controller;

import io.lettuce.core.RedisClient;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hsuhau
 * @date 2020/8/29 01:45
 */
@Configuration
public class WebSocketChatHandler extends TextWebSocketHandler {

    private static final Map<String, List<WebSocketSession>> roomUserMap = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 进入房间 -- 建立连接
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("新用户来了：" + session);
        // uri /redis-study-03/ws/chat/?userId=12001
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");
        String userId = uriComponents.getQueryParams().getFirst("userId");
        String score = uriComponents.getQueryParams().getFirst("score");
//        roomUserMap.put(roomId, session);
        redisTemplate.opsForZSet().add("roominfo::" + roomId, userId, Double.valueOf(score));
    }

    /**
     * 退出房间 -- 断开连接
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("用户离开:" + session);

        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");
        String userId = uriComponents.getQueryParams().getFirst("userId");
        String score = uriComponents.getQueryParams().getFirst("score");
        redisTemplate.opsForZSet().remove("roominfo::" + roomId, userId);
    }

    /**
     * 收到信息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到消息：" + session + " >> " + message);
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");
        String userId = uriComponents.getQueryParams().getFirst("userId");

        RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> redisCommands = connect.sync();
        redisCommands.xadd("redis:msg:" + roomId, "userId", userId, "content", message.getPayload());
    }

    /**
     * 启动系统后执行
     */
    @PostConstruct //初始化触发，spring默认是单实例，调用一次
    public void init() {
        new Thread(() -> {
            RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisCommands<String, String> redisCommands = connect.sync();
            while (true) {
                try {
                    Set<String> keySet = roomUserMap.keySet();
                    ArrayList<XReadArgs.StreamOffset<String>> streamOffsets = new ArrayList<>();

                    for (String roomId : keySet) {
                        streamOffsets.add(XReadArgs.StreamOffset.latest("room:msg:" + roomId));
                    }

                    if (streamOffsets.size() > 0) {
//                        List<StreamMessage<String, String>> stream_sms_send = redisCommands.xread(XReadArgs.Builder.block(10000), streamOffsets     );
//                        for (StreamMessage<String, String> streamMessage : stream_sms_send) {
//                            System.out.println(streamMessage);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // 推送消息 （其他人发的消息，推送到直播间）
    public void ReceiveRedisMessage(String data) {
// 推送房间号
    }
}
