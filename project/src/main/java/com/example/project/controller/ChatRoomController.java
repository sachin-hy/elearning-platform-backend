package com.example.project.controller;


import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.example.project.service.Interface.ChatRoomServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.service.ChatRoomService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ChatRoomController {

    @Autowired
	private ChatRoomServiceInterface chatRoomService;
	
	
	

	
   
	@PostMapping("/history/chatroom")
	public ResponseEntity<?> chatRoom(Principal principal)
	{
		
			String email = principal.getName();
			
			log.info("Get Chatroom history request recived for user : {}",email);
			List<Map<String,Object>> list = chatRoomService.ChatRooms(email);
	
			return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	
	
	
	@GetMapping("/history/{roomId}/message")
	public ResponseEntity<?> chatRoomMessage(@PathVariable("roomId") String rId)
	{
	
		   
		    log.info("get chat history request recived for roomid : {} ", rId);
		   List<Map<String,Object>>  message = chatRoomService.getMessage(rId);
		
		   return new ResponseEntity<>(message,HttpStatus.OK);
		
		
	}
}
