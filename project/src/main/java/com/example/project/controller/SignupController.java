package com.example.project.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.Signupdto;
import com.example.project.entity.OTPSchema;
import com.example.project.entity.Profile;
import com.example.project.entity.Users;
import com.example.project.service.EmailService;
import com.example.project.service.OTPSchemaService;
import com.example.project.service.SignUpService;
import com.example.project.service.UsersService;

@RestController
@RequestMapping("/auth")
public class SignupController {

	
	@Autowired
	private EmailService emailService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OTPSchemaService otpSchemaService;
	
	
	@Autowired
	private SignUpService signUpService;
	
	
	@PostMapping("/sendotp")
	public ResponseEntity<String> sendMail(@RequestParam("email") String email)
	{
		
		 signUpService.sendOtp(email);
		
	     return new ResponseEntity<>("Otp sent Successfully",HttpStatus.OK);
	  
		
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody Signupdto signupdto)
	{
		
			
			//entry create in db
			
			signUpService.saveUser(signupdto);
			
			
			//retrun res
			
			return new ResponseEntity<>("User registered Successfully",HttpStatus.OK);
		
		
	}
	
	
//	@GetMapping("/user")
//	public ResponseEntity<Users> getUserDetails(Principal principal)
//	{
//		try {
//			String email = principal.getName();
//			Users user = usersService.findByEmail(email);
//			return new ResponseEntity<>(user,HttpStatus.OK);
//		}catch(Exception e)
//		{
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
	
	
}
