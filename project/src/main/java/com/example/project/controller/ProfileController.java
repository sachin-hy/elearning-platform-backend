package com.example.project.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.project.dto.UpdateProfileRequest;
import com.example.project.dto.UserResponseDto;
import com.example.project.service.ProfileService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/user")
@Slf4j
public class ProfileController {

	private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
	
	
	
	
	
	@PutMapping("/update-profile")
	public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfile){
		
	         log.info("Update Profile Request Recived for user :{}",updateProfile.email());
			 UserResponseDto res = profileService.updateProfile(updateProfile); // usersService.updateUserAndProfile(updateProfile.firstName(),updateProfile.lastName(),updateProfile.email(),profile);
			 
			 return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
}
