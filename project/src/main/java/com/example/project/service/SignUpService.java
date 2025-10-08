package com.example.project.service;

import java.time.LocalDateTime;

import com.example.project.service.Interface.OTPSchemaServiceInterface;
import com.example.project.service.Interface.SignUpServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dto.Signupdto;
import com.example.project.entity.OTPSchema;
import com.example.project.entity.Users;
import com.example.project.enums.AccountType;
import com.example.project.exception.BadRequestException;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignUpService implements SignUpServiceInterface {

    @Autowired
	private  EmailService emailService;
    @Autowired
    private UsersServiceInterface userService;
    @Autowired
    private OTPSchemaServiceInterface otpSchemaService;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    
    
	
	@Transactional
	public void saveUser(Signupdto signupdto)
	{
		log.info("Attempting to save new user with email: {}", signupdto.email());
		
		if(userService.existByEmail(signupdto.email()))
		{
			log.warn("Sign-up failed: User already exists with email: {}", signupdto.email());
			
			throw new ConflictException("User Already Exist with The EmailId");
		}
		
		OTPSchema recentOtp  = otpSchemaService.findByemail(signupdto.email());
		
		
		if(recentOtp == null)
		{
			log.warn("Sign-up failed: No OTP found for email: {}", signupdto.email());
			
			throw new ResourceNotFoundException("Enter a valid Otp");
		}else if(!recentOtp.getOtp().equals(signupdto.otp()) || recentOtp.getExpiredAt().isBefore(LocalDateTime.now()))
		{
			log.warn("Sign-up failed: Invalid or expired OTP for email: {}", signupdto.email());
			
			throw new BadRequestException("The OTP provided is incorrect or has expired.");
		}
		
		Users user = new Users();
		
		user.setImage("https://api.dicebear.com/5.x/initials/svg?seed="+signupdto.firstName()+" "+signupdto.lastName());
	    user.setFirstName(signupdto.firstName());
	    user.setLastName(signupdto.lastName());
	    user.setEmail(signupdto.email());
	    user.setPassword(passwordEncoder.encode(signupdto.password()));

	    user.setAccountType(AccountType.valueOf(signupdto.accountType()));
        userService.save(user);

	    log.info("New user successfully created with email: {}", signupdto.email());
		
		
	}
	
	@Transactional
	public void sendOtp(String email)
	{
		log.info("Attempting to send OTP to email: {}", email);
		
		if(userService.existByEmail(email))
		{
			log.warn("OTP request failed: User already exists with email: {}", email);
			
			throw new ConflictException("User Already Exist with The EmailId");
		}
		
		String otp = RandomStringUtils.randomNumeric(6);
		

		try {
		  emailService.sendEmail(email, "otp verification email", otp);
		  log.info("OTP email sent successfully to: {}", email);
			
		}catch(Exception e)
		{
			log.error("Failed to send OTP email to {}: {}", email, e.getMessage(), e);
			
			throw new BadRequestException("Enter a Valid Email Address");
		}
		otpSchemaService.saveOTP(otp,email);
		log.info("OTP saved in database for email: {}", email);
		
	}
}
