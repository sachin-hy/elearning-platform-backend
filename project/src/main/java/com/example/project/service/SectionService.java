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


@Service
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
		
		Long cid = 0l;
		
		try {
			cid = Long.parseLong(sectionDto.courseid());
		}catch(NumberFormatException e)
		{
			throw new NumberFormatException("Enter valid courseid");
		}
		
		Courses course = courseRepo.findById(cid).get();
	
		Section section = new Section();
		section.setSectionName(sectionDto.sectionName());
		
		course.addSection(section);

		courseRepo.save(course);
		
	
		return new SectionResponseDto(section);
		
	}

	
	
	
	@Transactional
	public SectionResponseDto findByIdAndUpdate(Long sectionid,String sectionName) {
		// TODO Auto-generated method stub
		Section section = sectionRepo.updateSectionNameById(sectionid,sectionName);
		return new SectionResponseDto(section);
	}

	@Transactional
	public void deleteByid(String sectionid) {
		// TODO Auto-generated method stub
		
		Long sid ;
		try {
			sid = Long.parseLong(sectionid);
		}catch(NumberFormatException e)
		{
			throw new NumberFormatException("Enter valid sectionid");
		}
		
		if(!sectionRepo.existsById(sid))
		{
			throw new ResourceNotFoundException("No Section Found");
		}
		
		
		Section section = sectionRepo.findById(sid).get();
		
		section.removeSectionCourse();
		
		sectionRepo.deleteById(sid);
		

		
	}

	@Transactional
	public SectionResponseDto findByIdAndAddInSection(Long sectionid,SubSection subsection) {
		// TODO Auto-generated method stub
		Section section = sectionRepo.findById(sectionid).get();
		section.getSubSection().add(subsection);
		
		return new SectionResponseDto(section);
	}


	
	@Transactional
	public Section findById(Long sectionId)
	{
		return sectionRepo.findById(sectionId).get();
		
		
	}

	@Transactional
	public boolean existById(Long sectionid) {
		// TODO Auto-generated method stub
		return sectionRepo.existsById(sectionid);
	}
}
