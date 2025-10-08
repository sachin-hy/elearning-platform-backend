package com.example.project.service.Interface;

import com.example.project.dto.Signupdto;

public interface SignUpServiceInterface {

    public void saveUser(Signupdto signupdto);
    public void sendOtp(String email);
}
