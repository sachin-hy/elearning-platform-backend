package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfileDto(
	@NotBlank(message = "The GEnder Can Not be Null")	
    String gender,
    @NotBlank(message = "DOB can not be Null")
    String dob,
    @NotNull(message = "The About Part can not be null")
    String about,
    @NotBlank(message = "The Contact Nmber Can not be Null")
	@Size(min= 10,max = 10 ,message = "Enter a valid Contact numbers")
    String contactNumber
) {}
