package com.example.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.ProfileDto;
import com.example.project.dto.UpdateProfileRequest;
import com.example.project.dto.UserResponseDto;
import com.example.project.entity.Profile;
import com.example.project.entity.Users;
import com.example.project.repository.ProfileRepository;
import com.example.project.repository.UsersRepository;

@Service
public class ProfileService {

	@Autowired
	private ProfileRepository profileRepo;
	
	@Autowired
	private UsersRepository usersRepo;
	
	@Transactional
	public Profile saveProfile(ProfileDto profileDto) {
		// TODO Auto-generated method stub
		
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
		Users user = usersRepo.findByEmail(updateProfile.email());
		
		user.setFirstName(updateProfile.firstName());
		user.setLastName(updateProfile.lastName());
		
        Profile p = user.getAdditionalDetails();
		
		if(p == null)
		{
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
		
		return new UserResponseDto(user);
		
	}


	

}
