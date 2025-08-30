package com.example.project.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.project.entity.Section;
import com.example.project.entity.SubSection;

public record SectionResponseDto(
		 Long id,
		 String sectionName,
		 List<SubSectionResponseDto> subSection  
		) {
         
	 public SectionResponseDto(Section section)
	 {
		 this(section.getId(),
				 section.getSectionName(),
				 section.getSubSection() != null ?
				 section.getSubSection().stream()
				 .map(subsection -> new SubSectionResponseDto(subsection))
				 .collect(Collectors.toList())
				 :
				 new ArrayList<SubSectionResponseDto>()
			  );
	 }
	

}
