package com.example.project.service.Interface;

import com.example.project.entity.OTPSchema;

public interface OTPSchemaServiceInterface {
    public OTPSchema saveOTP(String otp, String email);

    public OTPSchema findByemail(String email);

}
