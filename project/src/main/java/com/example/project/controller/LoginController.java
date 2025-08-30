package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.LoginDto;
import com.example.project.dto.LoginResponseDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.entity.Users;
import com.example.project.service.SecurityCustomDetailService;
import com.example.project.service.UsersService;
import com.example.project.utilis.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class LoginController {
  
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private SecurityCustomDetailService securityCustomDetailService; 
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UsersService usersService;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto logindto,HttpServletRequest request)
	{
		
		
		if(logindto.email() == null || logindto.password() == null)
		{
		
			return new ResponseEntity<>("email  and password should be filled",HttpStatus.BAD_REQUEST);
		}
		
		
		LoginResponseDto res = usersService.checkCredentials(logindto,request);

			
		return new ResponseEntity<>(res,HttpStatus.OK);
	
	}
}
