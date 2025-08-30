package com.example.project.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.ResetPasswordDto;

import com.example.project.entity.Users;
import com.example.project.service.EmailService;
import com.example.project.service.UsersService;

@RestController
@RequestMapping("/auth")
public class ForgotPassword {

	@Autowired
	private UsersService usersService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam("email") String email
			                                     ,Principal princial)
	{
		
		try {
			//get email
			
			//check user for this email 
			Users user = usersService.findByEmail(email);
			
			if(user == null)
			{
				return new ResponseEntity<>("User did not Present",HttpStatus.BAD_REQUEST);
			}
			
			
			String token = RandomStringUtils.randomAlphanumeric(64);
			
			//update the user by adding token and expiration time
		     usersService.setUserToken(email,token);
			//create the url
		     String url = "Password Reset Link : "+"http://localhost:3000/update-password/"+token;
				
			//sned mail containg the url
		     emailService.sendEmail(user.getEmail(), "Password Reset Link", url);
		     
		     return new ResponseEntity<>("Email Sent successfuly ,please check your email to reset password",HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>("Error! Please try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	
	@PatchMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetDto)
	{
		
		try
		{
			
			//data fetch
			String token = resetDto.token();
			String password = resetDto.password();
			String confirmPassword = resetDto.confirmPassword();
			
			//validateion
			if(!password.equals(confirmPassword))
			{
				return new ResponseEntity<>("Password not matching",HttpStatus.BAD_REQUEST);
			}
			
			//get userdetails from db using token
			Users user = usersService.findBytoken(token);
			//check token is valid or not or token exist or not
			if(user == null)
			{
				return new ResponseEntity<>("Invalid Token",HttpStatus.NOT_FOUND);
			}
			
			if(user.getResetPasswordExpires().isBefore(LocalDateTime.now()))
			{
		       return new ResponseEntity<>("Token Expired",HttpStatus.BAD_REQUEST);		
			}
		
			// hash pwd
			String p = passwordEncoder.encode(confirmPassword);
			
			//update the password
			usersService.updatePassword(user.getEmail(),p);
			//return res
			
			return new ResponseEntity<>("Password updated",HttpStatus.OK);
		}catch(Exception e)
		{
			System.out.println("error / update - password = " + e);
			return new ResponseEntity<>("Error ! Please try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
}
