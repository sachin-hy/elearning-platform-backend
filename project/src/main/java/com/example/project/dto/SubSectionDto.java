package com.example.project.dto;

import org.springframework.web.multipart.MultipartFile;





public record SubSectionDto(
		 Long sectionid,
	        String title,
	        String timeDuration,
	        String description,
	        MultipartFile vedioUrl,
	        Long subsectionid
	        ) {}
