package com.example.project.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.RequestPasswordUpdateDto;
import com.example.project.service.EmailService;
import com.example.project.service.UsersService;

@RestController
public class ChangePasswordController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PatchMapping("/change-password")
	public ResponseEntity<String> updatePassword(@RequestBody RequestPasswordUpdateDto passwordDto
			                                     ,Principal principal)
	{
		if(!passwordDto.newPassword().equals(passwordDto.confirmedNewPassword()))
		{
			return new ResponseEntity<>("NewPassword and ConfirmNewPassword did not match",HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			String oldPassword = passwordEncoder.encode(passwordDto.oldPassword());
			String newPassword = passwordEncoder.encode(passwordDto.newPassword());
			
			String dbPassword = usersService.getPassword(principal.getName());
			 
			
			if(!dbPassword.equals(oldPassword))
			{
				return new ResponseEntity<>("Old Password did not match",HttpStatus.BAD_REQUEST);
			}
			
			usersService.updatePassword(principal.getName(),passwordDto.newPassword());
			
			String subject = "Password Updated Successfully";
			String message = "Your Password has been updated ";
			//send mail password updated
			emailService.sendEmail(principal.getName(), subject, message);
			
			return new ResponseEntity<>("Password Updated successfully",HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>("Internal server error .try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
	}
	
}
