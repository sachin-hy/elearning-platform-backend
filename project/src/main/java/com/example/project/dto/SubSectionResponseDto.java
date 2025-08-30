package com.example.project.dto;

import com.example.project.entity.SubSection;

public record SubSectionResponseDto(
		Long id,
		String title,
		String timeDuration,
		String description,
		String vedioUrl,
		String additionalUrl
		) 
{
    public SubSectionResponseDto(SubSection subsection)
    {
    	this(subsection.getId(),
    		 subsection.getTitle(),
    		 subsection.getTimeDuration(),
    		 subsection.getDescription(),
    		 subsection.getVedioUrl(),
    		 subsection.getAdditionalUrl()
    		 );
    }
}
