package com.example.project.dto;


import com.example.project.enums.AccountType;





public record LoginResponseDto ( 
		String userId,
		String token,
        AccountType accountType,
        String firstName,
   	    String lastName,
   	    String email,
   	    String image,
   	    String message,
        ProfileResponseDto additionalDetails
        ) 
{
	
	   public LoginResponseDto(UserResponseDto user,String jwt)
	   {
		   this(
				   user.userId(),
				   jwt,
				   user.accountType(),
				   user.firstName(),
				   user.lastName(),
				   user.email(),
				   user.image(),
				   "",
				   user.additionalDetails());
	   }
}
       
       
       
       
      
      