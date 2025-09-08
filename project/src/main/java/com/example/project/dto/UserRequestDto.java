package com.example.project.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDto {
	@Email(message="Enter a Valid Email Address")
	String email;
}
