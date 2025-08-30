package com.example.project.controller;

import javax.management.AttributeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.dto.SectionResponseDto;
import com.example.project.dto.SubSectionDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.entity.Section;
import com.example.project.entity.SubSection;
import com.example.project.service.SectionService;
import com.example.project.service.SubSectionService;
import com.example.project.utilis.ImageUploader;

@RestController
@RequestMapping("/subsection")
public class SubSectionController {

	@Autowired
	private ImageUploader imageUploader;
	@Autowired
	private SubSectionService subsectionService;
	@Autowired
	private SectionService sectionService;
	
	
	@PostMapping("/create-subsection")
	public ResponseEntity<?> createSubSection(@ModelAttribute SubSectionDto sectionDto)
	{
		
		    SubSectionResponseDto subsectionResponseDto = subsectionService.saveSubSection(sectionDto);
			
           
		    return new ResponseEntity<>(subsectionResponseDto,HttpStatus.OK);
		
	}
	
	
	
	//update subsectio
	@PutMapping("/update-subsection")
	public ResponseEntity<?> updateSubSection(@ModelAttribute SubSectionDto subsectionDto)
	 {
			SubSectionResponseDto subsectionResponseDto = subsectionService.findByIdAndUpdate(subsectionDto);//,section);
			//returnres
			return new ResponseEntity<>(subsectionResponseDto,HttpStatus.OK);
	
     }
	
	
	//delete subsection
	@DeleteMapping("/delete-subsection")
	public ResponseEntity<String> deleteSubSection(@RequestParam("subsectionid") String ssid)
    {
	     
		 
		    subsectionService.deleteById(ssid);
			
			 
			return new ResponseEntity<>("SubSection Deleted Successfully ",HttpStatus.OK);

		 
    }
	
	
	
} 
