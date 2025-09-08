package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
		@NotBlank(message= "name Field can Not Be Null")
		String name,
		@NotBlank(message = "Description Field Can Not Be Null")
	String description) {

	
	
	
	
}
