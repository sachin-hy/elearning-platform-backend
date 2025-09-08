package com.example.project.dto;


import com.example.project.entity.Users;
import com.example.project.enums.AccountType;

public record UserResponseDto(
		  String userId,
		  String email,
		  String firstName,
		  String lastName,
		  String image,
		 AccountType accountType,
		  ProfileResponseDto additionalDetails ) 
{
     public UserResponseDto(Users user)
     {
    	 this(
    			 user.getUserid().toString(),
    			 user.getEmail(),
    			 user.getFirstName(),
    			 user.getLastName(),
    			 user.getImage(),
    			 user.getAccountType(),
    			 user.getAdditionalDetails() != null ? new ProfileResponseDto(user.getAdditionalDetails()) : null
    			 );
     }
}
