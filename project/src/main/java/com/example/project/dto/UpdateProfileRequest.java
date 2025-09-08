package com.example.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
		@NotBlank(message = "First Name Field Can Not BE Null")
		String firstName,
		@NotBlank(message = "Last Name Field Can Not BE Null")
	 	  String lastName,
	 	 
	 	  String email,
	 	  @Size(min=10,max=10,message = "Enter valid Contact Number")
	      String contactNumber,
	      @NotBlank(message = "Gender Field Can Not BE Null")
	      String gender,
	      @NotBlank(message = "DOB can not Be Null")
	 	  String dob,
	 	  @NotEmpty(message = "About Field Can Not BE Empty")
	 	  String about
		)

{

	  
	
 	
}
