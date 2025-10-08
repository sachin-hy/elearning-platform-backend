package com.example.project.service;


import com.example.project.service.Interface.CourseServiceInterface;
import com.example.project.service.Interface.SectionServiceInterface;
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

import java.util.Optional;


@Service
@Slf4j
public class SectionService implements SectionServiceInterface {

    @Autowired
	private  SectionRepository sectionRepo;
    @Autowired
    private CourseServiceInterface courseService;

	
	
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
		
		Courses course = courseService.findById(cid);
	
		Section section = new Section();
		section.setSectionName(sectionDto.sectionName());
		
		course.addSection(section);

        courseService.save(course);
		log.info("Section '{}' saved successfully for course ID: {}", sectionDto.sectionName(), cid);
        
	
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

    @Override
    public boolean existsById(Long sectionid) {
        return sectionRepo.existsById(sectionid);
    }

    @Override
    public Optional<Section> findById(Long sectionid) {
        return sectionRepo.findById(sectionid);
    }

    @Override
    public void save(Section section) {
        sectionRepo.save(section);
    }

}
