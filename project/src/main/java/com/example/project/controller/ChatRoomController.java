package com.example.project.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.UserRequestDto;
import com.example.project.entity.Message;
import com.example.project.service.ChatRoomService;
import com.example.project.service.UsersService;

@RestController
public class ChatRoomController {

	
	
	@Autowired
	private ChatRoomService chatRoomService;
	
	@Autowired
	private UsersService usersService;
	
	
    //get chatroom of the user
	@PostMapping("/history/chatroom")
	public ResponseEntity<?> chatRoom(Principal principal)
	{
		try {
			
			System.out.println("/history/ charroom is called --------------------------------------------");
			
			String email = principal.getName();
			
			//get all the chatroom details from the user class
			List<Map<String,Object>> list = chatRoomService.ChatRooms(email);
			
			//retrun the user chat rooms 
			return new ResponseEntity<>(list,HttpStatus.OK);
			
			
		}catch(Exception e)
		{
			System.out.println("errro // get chatroom using userid = = = " + e);
			return new ResponseEntity<>(new ArrayList<>() ,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/* return the messages hisotry of the chatroom*/
	
	@GetMapping("/history/{roomId}/message")
	public ResponseEntity<?> chatRoomMessage(@PathVariable("roomId") String rId)
	{
		try {
		   Long roomId = Long.parseLong(rId);
		
		   List<Map<String,Object>>  message = chatRoomService.getMessage(roomId);
		
		   return new ResponseEntity<>(message,HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println("error = /chatroom/message " + e);
			
			return new ResponseEntity<>(new ArrayList<>() ,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
