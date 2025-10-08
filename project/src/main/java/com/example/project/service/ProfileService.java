package com.example.project.service;


import com.example.project.service.Interface.ProfileServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.ProfileDto;
import com.example.project.dto.UpdateProfileRequest;
import com.example.project.dto.UserResponseDto;
import com.example.project.entity.Profile;
import com.example.project.entity.Users;
import com.example.project.repository.ProfileRepository;
import com.example.project.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@Slf4j
public class ProfileService implements ProfileServiceInterface {

    @Autowired
	 private  ProfileRepository profileRepo;
    @Autowired
    private UsersServiceInterface usersService;

	@Transactional
	public UserResponseDto updateProfile(UpdateProfileRequest updateProfile)
	{
		log.info("Updating profile for user with email: {}", updateProfile.email());

		Users user = usersService.findByEmail(updateProfile.email()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		user.setFirstName(updateProfile.firstName());
		user.setLastName(updateProfile.lastName());

        Profile p = user.getAdditionalDetails();

		if(p == null)
		{
			 log.info("No existing profile found. Creating a new one for user: {}", user.getEmail());

			Profile newProfile = new Profile();
			newProfile.setAbout(updateProfile.about());
			newProfile.setContactNumber(updateProfile.contactNumber());
			newProfile.setDob(updateProfile.dob());
			newProfile.setGender(updateProfile.gender());
			user.setAdditionalDetails(newProfile);
		}else {
			p.setAbout(updateProfile.about());
			p.setContactNumber(updateProfile.contactNumber());
			p.setDob(updateProfile.dob());
			p.setGender(updateProfile.gender());
		}
		 log.info("Profile updated successfully for user: {}", user.getEmail());

		return new UserResponseDto(user);

	}


	

}
