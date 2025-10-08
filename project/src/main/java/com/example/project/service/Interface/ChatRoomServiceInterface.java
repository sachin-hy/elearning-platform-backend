package com.example.project.service.Interface;

import com.example.project.dto.MessageRequest;
import com.example.project.entity.ChatRoom;

import java.util.List;
import java.util.Map;

public interface ChatRoomServiceInterface {

    public List<Map<String,Object>> getMessage(String rId);
    public Map<String,Object> saveMessage(MessageRequest request, String email);
    public List<Map<String, Object>> ChatRooms(String email);
    public ChatRoom createChatRoom(String courseName, String thumbnail);

}
