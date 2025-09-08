package com.example.project.controller;

import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.RequestPasswordUpdateDto;
import com.example.project.service.ChangePasswordService;

import jakarta.validation.Valid;

@RestController
public class ChangePasswordController {
	
	private final ChangePasswordService changePasswordService;

	
	
	public ChangePasswordController(ChangePasswordService changePasswordService)
	{
		this.changePasswordService=changePasswordService;
	}
	
	
	
	
	@PatchMapping("/change-password")
	public ResponseEntity<String> updatePassword(@Valid @RequestBody RequestPasswordUpdateDto passwordDto
			                                     ,Principal principal)
	{
		    changePasswordService.updatePassword(passwordDto, principal.getName());
		    
		    
			return new ResponseEntity<>("Password Updated successfully",HttpStatus.OK);
		
	}
	
}
