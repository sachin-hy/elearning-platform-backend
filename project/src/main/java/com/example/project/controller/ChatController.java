package com.example.project.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.MessageRequest;
import com.example.project.entity.Users;
import com.example.project.service.ChatRoomService;
import com.example.project.service.UsersService;

@Controller
public class ChatController {

	@Autowired
	private ChatRoomService chatRoomService;
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chatroom/{roomId}")
	public void sendMessage(@DestinationVariable String roomId,MessageRequest message)
	{
		String email = message.getEmail();
		
		//Users user = userService.findByEmail(email);
		Map<String,Object> response = chatRoomService.saveMessage(message,email);
		
		messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, response);
		
	}
}
