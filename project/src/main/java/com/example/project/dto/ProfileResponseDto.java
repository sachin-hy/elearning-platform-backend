package com.example.project.dto;

import com.example.project.entity.Profile;

public record ProfileResponseDto(
		Long id,
		String gender,
		String dob,
		String about,
		String contactNumber
		) {
	
	public ProfileResponseDto(Profile profile)
	{
		this(
				profile.getId(),
				profile.getGender(),
				profile.getDob(),
				profile.getAbout(),
				profile.getContactNumber()
				);
	}

}
