package com.example.project.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record SectionDto(
		@NotBlank(message ="Section Name Can not be Null")
		String sectionName,
		@NotBlank(message = "CourseId can not be Null")
        String courseid
       
        
) {}
