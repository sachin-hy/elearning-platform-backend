package com.example.project.controller;


import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.service.ChatRoomService;

@RestController
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	
	
	
	public ChatRoomController(ChatRoomService chatRoomService)
	{
		this.chatRoomService = chatRoomService;
		
	}
	
   
	@PostMapping("/history/chatroom")
	public ResponseEntity<?> chatRoom(Principal principal)
	{
		
			String email = principal.getName();
			
			List<Map<String,Object>> list = chatRoomService.ChatRooms(email);
	
			return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	
	
	
	@GetMapping("/history/{roomId}/message")
	public ResponseEntity<?> chatRoomMessage(@PathVariable("roomId") String rId)
	{
	
		   
		
		   List<Map<String,Object>>  message = chatRoomService.getMessage(rId);
		
		   return new ResponseEntity<>(message,HttpStatus.OK);
		
		
	}
}
