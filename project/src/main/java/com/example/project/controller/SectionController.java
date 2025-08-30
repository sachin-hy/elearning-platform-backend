package com.example.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.AttributeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.SectionDto;
import com.example.project.dto.SectionResponseDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.entity.Courses;
import com.example.project.entity.Section;
import com.example.project.service.CourseService;
import com.example.project.service.SectionService;

@RestController
public class SectionController {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private CourseService courseService;
	
	
	
	@PostMapping("/create-section")
	public ResponseEntity<?> createSection(@RequestBody SectionDto sectionDto)
	{
	    
		     
		    SectionResponseDto sectionResponseDto = sectionService.saveSection(sectionDto);

			
			//return response
			return new ResponseEntity<>(sectionResponseDto,HttpStatus.OK);
		
		
		
	}
	

	

	
	
	
	@DeleteMapping("/delete-section")
	public ResponseEntity<String> deletesSection(@RequestParam("sectionid") String sid)
	{
		
			sectionService.deleteByid(sid);
		
	
			return new ResponseEntity<>("Section Deleted successfully",HttpStatus.OK);
		
	}
	
 	
	
}
