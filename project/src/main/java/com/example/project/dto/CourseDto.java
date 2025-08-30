package com.example.project.dto;

import org.springframework.web.multipart.MultipartFile;

public record CourseDto(
	    String courseName,
	    String courseDescription,
	    int price,
	    String category,
	    String whatyouwillLearn,
	    MultipartFile file,
	    String tag
	) {}
