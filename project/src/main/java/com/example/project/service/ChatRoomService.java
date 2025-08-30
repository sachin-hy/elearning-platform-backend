package com.example.project.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.dto.MessageRequest;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Message;
import com.example.project.entity.Users;
import com.example.project.repository.ChatRoomRepository;
import com.example.project.repository.MessageRepository;
import com.example.project.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatRoomService {
	
	
	@Autowired
	private ChatRoomRepository chatRepo;
	
	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private UsersRepository userRepo;
	
	/*return list of messages of the roomid*/
	@Transactional
	public List<Map<String,Object>> getMessage(Long roomId) {
		// TODO Auto-generated method stub
		 ChatRoom chatRoom = chatRepo.findById(roomId).get();
		 
		List<Message> message = messageRepo.findMessageByChatRoom(chatRoom);
		
		System.out.println("message list size = " + message.size());
		return message.stream().map(msg -> {
			HashMap<String,Object> m = new HashMap<>();
			m.put("userId", msg.getUser().getUserid());
			m.put("messageId", msg.getId());
			m.put("senderName", msg.getSenderName());
			m.put("content", msg.getContent());
			m.put("senderImageUrl", msg.getSenderImageUrl());
			m.put("timeStamp",msg.getTime());
			return m;
		}).collect(Collectors.toList());
	}


	@Transactional
	public ChatRoom findById(String roomId) {
		
		Long rId = Long.parseLong(roomId);
		return chatRepo.findById(rId).get();
		
		
	}


	@Transactional
	public Map<String,Object> saveMessage(MessageRequest request,String email) {
		// TODO Auto-generated method stub
		Long rId = Long.parseLong(request.getRoomId());
		System.out.println("message save is called ===================");
		
		
		ChatRoom chatRoom = chatRepo.findById(rId).get();
		Users user = userRepo.findByEmail(email);
		
		
		Message m = new Message();
		m.setContent(request.getContent());
		m.setSenderName(user.getFullName());
		m.setTime(LocalDateTime.now());
		m.setSenderImageUrl(user.getImage());
		
		m.addUser(user);
		m.addChatRoom(chatRoom);
		Message msg = messageRepo.save(m);
		
		HashMap<String,Object> messageResponse = new HashMap<>();
		messageResponse.put("userId", msg.getUser().getUserid());
		messageResponse.put("messageId", msg.getId());
		messageResponse.put("senderName", msg.getSenderName());
		messageResponse.put("content", msg.getContent());
		messageResponse.put("senderImageUrl", msg.getSenderImageUrl());
		messageResponse.put("timeStamp",msg.getTime());
		return messageResponse;
		
	}
	
	
	@Transactional
    public List<Map<String, Object>> ChatRooms(String email) {
        List<ChatRoom> list = chatRepo.findChatRoomsByEmail(email);

        System.out.println("chat room list isze in userservic chatroom = " + list.size());
        return list.stream()
            .map(room -> {
                Map<String, Object> map = new HashMap<>();
                map.put("roomId", room.getId());
                map.put("roomName", room.getRoomName());
                map.put("roomImageUrl", room.getCourseImageUrl());
                return map;
            })
            .collect(Collectors.toList());
    }


}
