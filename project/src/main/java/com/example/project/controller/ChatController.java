package com.example.project.controller;

import java.util.Map;

import com.example.project.service.Interface.ChatRoomServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.project.dto.MessageRequest;

import com.example.project.service.ChatRoomService;

import jakarta.validation.Valid;



@Controller
public class ChatController {

	@Autowired
	private ChatRoomServiceInterface chatRoomService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chatroom/{roomId}")
	public void sendMessage(@DestinationVariable String roomId,@Valid MessageRequest message)
	{
		String email = message.getEmail();
		
		Map<String,Object> response = chatRoomService.saveMessage(message,email);
		
		messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, response);
		
	}
}
