package com.example.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginDto(

	 @NotBlank(message = "Email Field Can not be Null")
	 @Email(message = "Enter a Valid Email Address")
	 String email,
	 @NotBlank(message = "The Password Field Can Not be Null")
	 @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#%&])[A-Za-z0-9@#%&]{8,10}$",
	    message = "Not a Valid Password")
	 String password
	
	
) {}
