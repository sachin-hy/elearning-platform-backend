package com.example.project.service;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
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

@Service
public class SignUpService {

	private final EmailService emailService;
    private final UsersRepository userRepo;
    private final OTPSchemaService otpSchemaService;
    private final PasswordEncoder passwordEncoder;

    public SignUpService(EmailService emailService, UsersRepository userRepo,
                         OTPSchemaService otpSchemaService, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.otpSchemaService = otpSchemaService;
        this.passwordEncoder = passwordEncoder;
    }
    
    
	
	@Transactional
	public void saveUser(Signupdto signupdto)
	{
		if(userRepo.existByEmail(signupdto.email()))
		{
			throw new ConflictException("User Already Exist with The EmailId");
		}
		
		OTPSchema recentOtp  = otpSchemaService.findByemail(signupdto.email());
		
		
		if(recentOtp == null)
		{
			throw new ResourceNotFoundException("Enter a valid Otp");
		}else if(!recentOtp.getOtp().equals(signupdto.otp()) || recentOtp.getExpiredAt().isBefore(LocalDateTime.now()))
		{
			throw new BadRequestException("The OTP provided is incorrect or has expired.");
		}
		
		Users user = new Users();
		
		user.setImage("https://api.dicebear.com/5.x/initials/svg?seed="+signupdto.firstName()+" "+signupdto.lastName());
	    user.setFirstName(signupdto.firstName());
	    user.setLastName(signupdto.lastName());
	    user.setEmail(signupdto.email());
	    user.setPassword(passwordEncoder.encode(signupdto.password()));
	    user.setAccountType(null);
	    user.setAccountType(AccountType.valueOf(signupdto.accountType()));
	    userRepo.save(user);

		
		
	}
	
	@Transactional
	public void sendOtp(String email)
	{
		if(userRepo.existByEmail(email))
		{
			throw new ConflictException("User Already Exist with The EmailId");
		}
		
		String otp = RandomStringUtils.randomNumeric(6);
		
		System.out.println(otp);
		try {
		  emailService.sendEmail(email, "otp verification email", otp);
		}catch(Exception e)
		{
			throw new BadRequestException("Enter a Valid Email Address");
		}
		otpSchemaService.saveOTP(otp,email);
		
	}
}
