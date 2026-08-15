package com.example.project.messaging;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.example.project.dto.ChatMessageEvent;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class RedisChatPublisher {

    @Autowired
    @Qualifier("chatRedisTemplate")
    private RedisTemplate<String, Object> chatRedisTemplate;

    @Autowired
    @Qualifier("chatRoomTopic")
    private ChannelTopic chatRoomTopic;

    public void publish(String roomId, Map<String, Object> payload) {
        log.info("Publishing chat message to Redis channel [{}] for room {}", chatRoomTopic.getTopic(), roomId);
        ChatMessageEvent event = new ChatMessageEvent(roomId, payload);
        chatRedisTemplate.convertAndSend(chatRoomTopic.getTopic(), event);
    }
}
