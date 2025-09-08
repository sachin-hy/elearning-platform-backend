package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.LoginDto;
import com.example.project.dto.LoginResponseDto;
import com.example.project.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class LoginController {
  
	
	 private final UsersService usersService;

	 public LoginController(UsersService usersService) {
	        this.usersService = usersService;
	 }
	
	 
	 
	@PostMapping("/login")
	public ResponseEntity<?> login( @Valid @RequestBody LoginDto logindto,HttpServletRequest request)
	{
		log.info("Login request received for user: {}",logindto.email());
		LoginResponseDto res = usersService.checkCredentials(logindto,request);

			
		return new ResponseEntity<>(res,HttpStatus.OK);
	
	}
}
