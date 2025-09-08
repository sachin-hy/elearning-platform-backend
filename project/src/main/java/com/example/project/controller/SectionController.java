package com.example.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.SectionDto;
import com.example.project.dto.SectionResponseDto;
import com.example.project.service.SectionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SectionController {

	 private final SectionService sectionService;

	    public SectionController(SectionService sectionService) {
	        this.sectionService = sectionService;
	    }
	
	
	@PostMapping("/create-section")
	public ResponseEntity<?> createSection(@Valid @RequestBody SectionDto sectionDto)
	{
	        log.info("Create Section Request Recived for sectionname : {} ",sectionDto.sectionName());
		     
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
