package com.example.project.service.Interface;

import com.example.project.dto.UpdateProfileRequest;
import com.example.project.dto.UserResponseDto;

public interface ProfileServiceInterface {

    public UserResponseDto updateProfile(UpdateProfileRequest updateProfile);
}
