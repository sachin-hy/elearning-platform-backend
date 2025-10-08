package com.example.project.service;

import com.example.project.service.Interface.ChangePasswordServiceInterface;
import com.example.project.service.Interface.EmailServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dto.RequestPasswordUpdateDto;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChangePasswordService implements ChangePasswordServiceInterface {



	@Autowired
	private UsersServiceInterface usersService;
    @Autowired
	private EmailServiceInterface emailService;
    @Autowired
	private  PasswordEncoder passwordEncoder;

	
	@Transactional
	public void updatePassword(RequestPasswordUpdateDto passwordDto,String email)
	{
		log.info("Attempting password update for user: {}", email);
		
		if(!passwordDto.newPassword().equals(passwordDto.confirmedNewPassword()))
		{
			log.warn("Password update failed for user {}: new passwords did not match.", email);
			
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
		log.info("Password updated successfully for user: {}", email);
		
		String subject = "Password Updated Successfully";
		String message = "Your Password has been updated ";
		//send mail password updated
		emailService.sendEmail(email, subject, message);
		log.info("Password update confirmation email sent to user: {}", email);
		
	}
}
