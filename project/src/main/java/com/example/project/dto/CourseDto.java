package com.example.project.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CourseDto(
		@NotBlank(message="Course Name Can Not be Null")
	    String courseName,
	    @NotBlank(message="Course Description Can Not be Null")
	    String courseDescription,
	    @Min(value = 1, message = "Price must be greater than zero")
	    int price,
	    @NotBlank(message = "The Category Value can not be Null")
	    String category,
	    String whatyouwillLearn,
	    MultipartFile file,
	    @NotBlank(message = "Tag Field can not be null")
	    String tag
	) {}
