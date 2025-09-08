package com.example.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Signupdto (
		@NotBlank(message = "First Name Field Can Not BE Null")
		String firstName,
		@NotBlank(message = "Last Name Field Can Not BE Null")
		 String lastName,
		 @Email(message = "Enter  a valid Emial Address")
		 String email,
		 
		 String contactNumber,
		 @NotEmpty(message = "Password Can not be null")
		 @Size(min = 8, max = 10 ,message = "Password Must be 8-10 charater Long")
		 @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#%&])[A-Za-z0-9@#%&]{8,10}$",
		 message = "Password must be 8-10 characters long and include at least one uppercase letter, one number, and one of the following special characters: @#%&")
		 String password,
		 @NotEmpty(message = "Password Can not be null")
		 @Size(min = 8, max = 10 ,message = "Password Must be 8-10 charater Long")
	     @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#%&])[A-Za-z0-9@#%&]{8,10}$",
		 message = "Password must be 8-10 characters long and include at least one uppercase letter, one number, and one of the following special characters: @#%&")
		 String confirmPassword ,
		 
		 @NotBlank(message = "Otp Filed Can not be Null")
		 String otp,
		 @NotBlank(message = "Account Type can not be null")
		 String accountType	
		
		)
{}
