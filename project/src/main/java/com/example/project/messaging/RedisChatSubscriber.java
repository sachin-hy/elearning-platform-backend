package com.example.project.messaging;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.project.dto.ChatMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class RedisChatSubscriber implements org.springframework.data.redis.connection.MessageListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    @Qualifier("chatRedisTemplate")
    private RedisTemplate<String, Object> chatRedisTemplate;

    @Autowired
    @Qualifier("chatObjectMapper")
    private ObjectMapper chatObjectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) chatRedisTemplate.getValueSerializer();
            Object raw = valueSerializer.deserialize(message.getBody());


            ChatMessageEvent event = chatObjectMapper.convertValue(raw, ChatMessageEvent.class);

            log.info("Relaying chat message from Redis to local clients for room {}", event.getRoomId());
            messagingTemplate.convertAndSend("/topic/chatroom/" + event.getRoomId(), event.getPayload());
        } catch (Exception e) {
            log.error("Failed to process chat message from Redis channel", e);
        }
    }
}
