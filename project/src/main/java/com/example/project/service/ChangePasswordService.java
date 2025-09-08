package com.example.project.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dto.RequestPasswordUpdateDto;

import jakarta.transaction.Transactional;

@Service
public class ChangePasswordService {
	
	private final UsersService usersService;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	
	public ChangePasswordService(UsersService usersService,  
			                     EmailService emailService,
			                     PasswordEncoder passwordEncoder)
	{
		this.emailService = emailService;
		this.usersService = usersService;
		this.passwordEncoder = passwordEncoder;
	}

	
	
	
	
	
	
	@Transactional
	public void updatePassword(RequestPasswordUpdateDto passwordDto,String email)
	{
		if(!passwordDto.newPassword().equals(passwordDto.confirmedNewPassword()))
		{
			throw new IllegalArgumentException("NewPassword and ConfirmNewPassword did not match");
		}
		
		String oldPassword = passwordEncoder.encode(passwordDto.oldPassword());
		String newPassword = passwordEncoder.encode(passwordDto.newPassword());
	
		String dbPassword = usersService.getPassword(email);
		
		if(!dbPassword.equals(oldPassword))
		{
			throw new IllegalArgumentException("Old Password did not match");
		}
		
		
		usersService.updatePassword(email,newPassword);
		
		String subject = "Password Updated Successfully";
		String message = "Your Password has been updated ";
		//send mail password updated
		emailService.sendEmail(email, subject, message);
		
	}
}
