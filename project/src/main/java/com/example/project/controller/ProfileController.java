package com.example.project.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.LoginResponseDto;
import com.example.project.dto.ProfileDto;

import com.example.project.dto.UpdateProfileRequest;
import com.example.project.dto.UserResponseDto;
import com.example.project.entity.Profile;
import com.example.project.entity.Users;
import com.example.project.service.ProfileService;
import com.example.project.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/user")
public class ProfileController {

	@Autowired
	private ProfileService profileService;
	@Autowired
	private UsersService usersService;
	
	
	
	
	
	
	@PutMapping("/update-profile")
	public ResponseEntity<UserResponseDto> updateProfile(@RequestBody UpdateProfileRequest updateProfile){
		
	
			 UserResponseDto res = profileService.updateProfile(updateProfile); // usersService.updateUserAndProfile(updateProfile.firstName(),updateProfile.lastName(),updateProfile.email(),profile);
			 
			 return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
}
