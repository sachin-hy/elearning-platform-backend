package com.example.project.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.project.service.Interface.ChatRoomServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class ChatRoomService implements ChatRoomServiceInterface {

    @Autowired
	private  ChatRoomRepository chatRepo;
    @Autowired
    private  MessageRepository messageRepo;
    @Autowired
    private UsersServiceInterface usersService;
	
	
    
    
    
    
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
		log.info("Found {} messages for chat room ID: {}", roomId);
		
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
	public Map<String,Object> saveMessage(MessageRequest request,String email) {
		// TODO Auto-generated method stub
		log.info("Attempting to save message to room ID: {} from user: {}", request.getRoomId(), email);
		
		Long rId = Long.parseLong(request.getRoomId());
		
		
		Optional<ChatRoom> cr = chatRepo.findById(rId);

        if(cr.isEmpty())
        {
           throw new ResourceNotFoundException("No Chat Room Found .Please Try After Some Time");
        }

		Optional<Users> u = usersService.findByEmail(email);

        if(u.isEmpty())
        {
            throw new UsernameNotFoundException("User not found");
        }
        Users user  = u.get();
        ChatRoom chatRoom = cr.get();

		Message m = new Message();
		m.setContent(request.getContent());
		m.setSenderName(user.getFullName());
		m.setTime(LocalDateTime.now());
		m.setSenderImageUrl(user.getImage());
		
		m.addUser(user);
		m.addChatRoom(chatRoom);
		Message msg = messageRepo.save(m);


		log.info("Message saved successfully with ID: {}");
		
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

    @Transactional
    public ChatRoom createChatRoom(String courseName,String thumbnail)
    {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(courseName);
        chatRoom.setCourseImageUrl(thumbnail);

       return chatRepo.save(chatRoom);
    }


}
