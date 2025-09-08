package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.SectionDto;
import com.example.project.dto.SectionResponseDto;
import com.example.project.entity.Courses;
import com.example.project.entity.Section;
import com.example.project.entity.SubSection;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.SectionRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class SectionService {

	private final SectionRepository sectionRepo;
    private final CourseRepository courseRepo;

    public SectionService(SectionRepository sectionRepo, CourseRepository courseRepo) {
        this.sectionRepo = sectionRepo;
        this.courseRepo = courseRepo;
    }
	
	
	@Transactional
	public SectionResponseDto saveSection(SectionDto sectionDto) {
		// TODO Auto-generated method stub
		 log.info("Attempting to save a new section: {} for course ID: {}", sectionDto.sectionName(), sectionDto.courseid());

		Long cid = 0l;
		
		try {
			cid = Long.parseLong(sectionDto.courseid());
		}catch(NumberFormatException e)
		{
			  log.warn("Invalid course ID format: {}", sectionDto.courseid());
	          
			throw new NumberFormatException("Enter valid courseid");
		}
		
		Courses course = courseRepo.findById(cid).get();
	
		Section section = new Section();
		section.setSectionName(sectionDto.sectionName());
		
		course.addSection(section);

		courseRepo.save(course);
		log.info("Section '{}' saved successfully for course ID: {}", sectionDto.sectionName(), cid);
        
	
		return new SectionResponseDto(section);
		
	}

	
	
	
	@Transactional
	public SectionResponseDto findByIdAndUpdate(Long sectionid,String sectionName) {
		// TODO Auto-generated method stub
		log.info("Updating section ID: {} to new name: {}", sectionid, sectionName);
        
		Section section = sectionRepo.updateSectionNameById(sectionid,sectionName);
		
		  log.info("Section ID: {} updated successfully.", sectionid);
	      
		return new SectionResponseDto(section);
	}

	@Transactional
	public void deleteByid(String sectionid) {
		// TODO Auto-generated method stub
		
		log.info("Attempting to delete section with ID: {}", sectionid);

		Long sid ;
		try {
			sid = Long.parseLong(sectionid);
		}catch(NumberFormatException e)
		{
			log.warn("Invalid section ID format for deletion: {}", sectionid);
	           
			throw new NumberFormatException("Enter valid sectionid");
		}
		
		if(!sectionRepo.existsById(sid))
		{
			throw new ResourceNotFoundException("No Section Found");
		}
		
		
		Section section = sectionRepo.findById(sid).get();
		
		section.removeSectionCourse();
		
		sectionRepo.deleteById(sid);
		
		 log.info("Section with ID: {} deleted successfully.", sid);
		
	}

	@Transactional
	public SectionResponseDto findByIdAndAddInSection(Long sectionid,SubSection subsection) {
		// TODO Auto-generated method stub
		
		 log.info("Adding sub-section with ID: {} to section ID: {}", subsection.getId(), sectionid);

		Section section = sectionRepo.findById(sectionid).get();
		section.getSubSection().add(subsection);
		log.info("Sub-section added to section ID: {}", sectionid);
        
		return new SectionResponseDto(section);
	}


	
	@Transactional
	public Section findById(Long sectionId)
	{
		 log.info("Finding section by ID: {}", sectionId);
		return sectionRepo.findById(sectionId).get();
		
		
	}

	@Transactional
	public boolean existById(Long sectionid) {
		// TODO Auto-generated method stub
		log.info("Checking for existence of section with ID: {}", sectionid);
	       
		return sectionRepo.existsById(sectionid);
	}
}
