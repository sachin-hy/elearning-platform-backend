package com.example.project.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.OTPSchema;
import com.example.project.repository.OtpSchemaRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OTPSchemaService {
	
	private final OtpSchemaRepository otpRepo;

    public OTPSchemaService(OtpSchemaRepository otpRepo) {
        this.otpRepo = otpRepo;
    }
	
	@Transactional
	public Optional<OTPSchema> findByotp(String otp)
	{
		log.info("Finding OTP by value: {}", otp);
		return otpRepo.findByotp(otp);
	}


	@Transactional
	public OTPSchema saveOTP(String otp, String email) {
		//create an entry in db for otp
		
		log.info("Generating and saving OTP for email: {}", email);
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expire = LocalDateTime.now().plusMinutes(10);
	
		OTPSchema otpSchema = new OTPSchema();
		otpSchema.setEmail(email);
		otpSchema.setOtp(otp);
		otpSchema.setCreatedAt(now);
		otpSchema.setExpiredAt(expire);
			
		
		OTPSchema savedOtp = otpRepo.save(otpSchema);
		log.info("New OTP saved for email {} with ID: {}", email, savedOtp.getId());
		
		return savedOtp;
		
	}


	@Transactional
	public OTPSchema findByemail(String email) {
		log.info("Finding OTP by email: {}", email);
		
		OTPSchema otpList = otpRepo.findByemail(email);
		if(otpList == null)
		{
			return null;
		}

		return otpList;
	}

}
