package com.example.project.service.Interface;

import com.example.project.dto.RequestPasswordUpdateDto;

public interface ChangePasswordServiceInterface {
    public void updatePassword(RequestPasswordUpdateDto passwordDto, String email);


}
