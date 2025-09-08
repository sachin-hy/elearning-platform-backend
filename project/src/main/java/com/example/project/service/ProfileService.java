package com.example.project.service;


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

@Service
@Slf4j
public class ProfileService {

	 private final ProfileRepository profileRepo;
	    private final UsersRepository usersRepo;

	    public ProfileService(ProfileRepository profileRepo, UsersRepository usersRepo) {
	        this.profileRepo = profileRepo;
	        this.usersRepo = usersRepo;
	    }
	
	@Transactional
	public Profile saveProfile(ProfileDto profileDto) {
		// TODO Auto-generated method stub
		 log.info("Saving a new standalone profile.");
		Profile profile=new Profile();
		profile.setGender(profileDto.gender());
		profile.setAbout(profileDto.about());
		profile.setContactNumber(profileDto.contactNumber());
		profile.setDob(profileDto.dob());
		
		return profileRepo.save(profile);
	}
	
	
	@Transactional
	public UserResponseDto updateProfile(UpdateProfileRequest updateProfile)
	{ 
		log.info("Updating profile for user with email: {}", updateProfile.email());

		Users user = usersRepo.findByEmail(updateProfile.email());
		
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
