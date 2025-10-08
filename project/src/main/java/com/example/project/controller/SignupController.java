package com.example.project.controller;



import com.example.project.service.Interface.SignUpServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.Signupdto;
import com.example.project.service.SignUpService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class SignupController {



    @Autowired
	 private SignUpServiceInterface signUpService;


	    
	
	@PostMapping("/sendotp")
	public ResponseEntity<String> sendMail(@RequestParam("email") String email)
	{
		log.info("send otp request recived for user : {}",email);
		
		 signUpService.sendOtp(email);
		
	     return new ResponseEntity<>("Otp sent Successfully",HttpStatus.OK);
	  
		
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody Signupdto signupdto)
	{
		
			
			//entry create in db
			log.info("signup request recieved for user ;{}",signupdto.email());
			signUpService.saveUser(signupdto);
			
			
			//retrun res
			
			return new ResponseEntity<>("User registered Successfully",HttpStatus.OK);
		
		
	}
	
	
	
}
