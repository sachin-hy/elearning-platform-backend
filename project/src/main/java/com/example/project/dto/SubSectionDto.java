package com.example.project.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;





public record SubSectionDto(
		    @NotNull(message = "Section id Can Not be Null")
		    Long sectionid,
		    @NotBlank(message = "Title Can Not be Null")
	        String title,
	        @NotBlank(message = "Time Duration Can Not be null")
	        String timeDuration,
	        @NotBlank(message = "Description Filed Can Not Be null")
	        String description,
	        
	        MultipartFile vedioUrl,
	        
	        Long subsectionid
	        ) {}
