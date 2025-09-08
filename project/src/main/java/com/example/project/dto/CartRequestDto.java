package com.example.project.dto;

import jakarta.validation.constraints.NotNull;

public class CartRequestDto {
	@NotNull(message = "CourseId can not be Null")
	public Long courseid;
	
}
