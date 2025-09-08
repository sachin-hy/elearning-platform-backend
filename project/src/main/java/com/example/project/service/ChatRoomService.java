package com.example.project.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.example.project.dto.MessageRequest;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Message;
import com.example.project.entity.Users;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.ChatRoomRepository;
import com.example.project.repository.MessageRepository;
import com.example.project.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatRoomService {
	
	
	private final ChatRoomRepository chatRepo;
    private final MessageRepository messageRepo;
    private final UsersRepository userRepo;
	
	
    public ChatRoomService(ChatRoomRepository chatRepo, MessageRepository messageRepo, UsersRepository userRepo) {
             this.chatRepo = chatRepo;
             this.messageRepo = messageRepo;
             this.userRepo = userRepo;
         }
    
    
    
    
	@Transactional
	public List<Map<String,Object>> getMessage(String rId) {
		// TODO Auto-generated method stub
		log.info("Attempting to retrieve messages for chat room ID: {}", rId);
		
		 Long roomId;
		
		try {
			roomId = Long.parseLong(rId);
		}catch(Exception e)
		{
			log.warn("Invalid chat room ID provided: {}", rId);
			
			throw new NumberFormatException();
		}
		
		
		ChatRoom chatRoom = chatRepo.findById(roomId).get();
		 
		List<Message> message = messageRepo.findMessageByChatRoom(chatRoom);
		log.info("Found {} messages for chat room ID: {}", messages.size(), roomId);
		
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
		
		log.info("Finding chat room by ID: {}", roomId);
		
		Long rId = Long.parseLong(roomId);
		return chatRepo.findById(rId).get();
		
		
	}


	@Transactional
	public Map<String,Object> saveMessage(MessageRequest request,String email) {
		// TODO Auto-generated method stub
		log.info("Attempting to save message to room ID: {} from user: {}", request.getRoomId(), email);
		
		Long rId = Long.parseLong(request.getRoomId());
		
		
		ChatRoom chatRoom = chatRepo.findById(rId).get();
		Users user = userRepo.findByEmail(email);
		
		if (user == null) {
			log.warn("User not found for email: {}", email);
			throw new ResourceNotFoundException("User not found.");
		}
		Message m = new Message();
		m.setContent(request.getContent());
		m.setSenderName(user.getFullName());
		m.setTime(LocalDateTime.now());
		m.setSenderImageUrl(user.getImage());
		
		m.addUser(user);
		m.addChatRoom(chatRoom);
		Message msg = messageRepo.save(m);
		log.info("Message saved successfully with ID: {}", savedMessage.getId());
		
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
        
		log.info("Fetching chat rooms for user with email: {}", email);
        
		
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
