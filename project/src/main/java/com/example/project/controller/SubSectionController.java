package com.example.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.project.dto.SubSectionDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.service.SubSectionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/subsection")
@Slf4j
public class SubSectionController {

	
	 private final SubSectionService subsectionService;

	    public SubSectionController(SubSectionService subsectionService) {
	        this.subsectionService = subsectionService;
	    }
	
	
	@PostMapping("/create-subsection")
	public ResponseEntity<?> createSubSection(@Valid @ModelAttribute SubSectionDto sectionDto)
	{
		
		  log.info("create subsection request recived for subsection name : {}",sectionDto.title());
		    SubSectionResponseDto subsectionResponseDto = subsectionService.saveSubSection(sectionDto);
			
           
		    return new ResponseEntity<>(subsectionResponseDto,HttpStatus.OK);
		
	}
	
	
	
	
	@PutMapping("/update-subsection")
	public ResponseEntity<?> updateSubSection(@Valid @ModelAttribute SubSectionDto subsectionDto)
	 {
		   log.info("update susection request recived for subsection name : {}",subsectionDto.title());
			SubSectionResponseDto subsectionResponseDto = subsectionService.findByIdAndUpdate(subsectionDto);//,section);
			//returnres
			return new ResponseEntity<>(subsectionResponseDto,HttpStatus.OK);
	
     }
	
	
	
	@DeleteMapping("/delete-subsection")
	public ResponseEntity<String> deleteSubSection(@RequestParam("subsectionid") String ssid)
    {
	     log.info("delete subsection request recived for subsection id : {}",ssid);
		 
		    subsectionService.deleteById(ssid);
			
			 
			return new ResponseEntity<>("SubSection Deleted Successfully ",HttpStatus.OK);

		 
    }
	
	
	
} 
