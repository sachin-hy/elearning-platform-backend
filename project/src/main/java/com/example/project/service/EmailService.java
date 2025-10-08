package com.example.project.service;

import com.example.project.service.Interface.EmailServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService implements EmailServiceInterface {

    @Autowired
	private  JavaMailSender mailSender;

    
	
	@Transactional
	public void sendEmail(String to,String subject,String text)
	{
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(to);
	    message.setSubject(subject);
	    message.setText(text);
	    mailSender.send(message);
	}
}
