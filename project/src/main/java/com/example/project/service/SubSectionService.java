package com.example.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.SubSectionDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.entity.Section;
import com.example.project.entity.SubSection;
import com.example.project.exception.InternalServerError;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.SectionRepository;
import com.example.project.repository.SubSectionRepository;
import com.example.project.utilis.ImageUploader;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SubSectionService {
	
	@Autowired
	private SubSectionRepository subRepo;
	@Autowired
	private SectionRepository sectionRepo;

	@Autowired
	private ImageUploader imageUploader;
	
	
	@Transactional
	public SubSectionResponseDto saveSubSection(SubSectionDto sectionDto) {
		
		if(!sectionRepo.existsById(sectionDto.sectionid()))
		{
			throw new ResourceNotFoundException("Enter Valid Section Id");
		}
		
		String vedioUrl = "";
		try {
		 vedioUrl = imageUploader.uploadFile(sectionDto.vedioUrl());
		}catch(Exception e)
		{
			throw new InternalServerError("Server Error Please Try After Some Time");
		}
		
		
		Section section = sectionRepo.findById(sectionDto.sectionid()).get();
		
		SubSection subsection = new SubSection();
		subsection.setTitle(sectionDto.title());
		subsection.setTimeDuration(sectionDto.timeDuration());
		subsection.setDescription(sectionDto.description());
		subsection.setVedioUrl(vedioUrl);
		
		subsection.addSection(section);
		
		sectionRepo.save(section);
		return new SubSectionResponseDto(subsection);
	    
	}

	
	
	
	@Transactional
	public SubSectionResponseDto findByIdAndUpdate(SubSectionDto subsectionDto) {
		
		Long subsectionid = (Long) subsectionDto.subsectionid();
		
		if(!subRepo.existsById(subsectionid))
		{
			throw new ResourceNotFoundException("Enter Valid SubsectiionId");
		}
		
		String vedioUrl = "";
		try {
		 vedioUrl = imageUploader.uploadFile(subsectionDto.vedioUrl());
		}catch(Exception e)
		{
			throw new InternalServerError("Server Error Please Try After Some Time");
		}
		
		
		SubSection subsection = subRepo.findById(subsectionid).get();
		
		if(subsectionDto.title() != null)
		{
		  subsection.setTitle(subsectionDto.title());
		}
		
		if(subsectionDto.timeDuration() != null)
		{
		  subsection.setTimeDuration(subsectionDto.timeDuration());
		}
		
		if(subsectionDto.description() != null)
		{
		  subsection.setDescription(subsectionDto.description());
		}
		subsection.setVedioUrl(vedioUrl);
		
		
		return new SubSectionResponseDto(subsection);
		
	}

	
	
	@Transactional
	public void deleteById(String id) {
		
		Long subsectionid = 0l;
		try {
			subsectionid = Long.parseLong(id);
		}catch(Exception e)
		{
			throw new NumberFormatException("Enter Valid Subsection Id");
		}
		
		if(!subRepo.existsById(subsectionid))
		{
			throw new ResourceNotFoundException("Enter Valid Subsection Id");
		}
		
		
		SubSection subsection = subRepo.findById(subsectionid).get();
		subsection.removeSection();
		
		subRepo.deleteById(subsectionid);
		
//		Section section = subsection.getSection();
//		section.getSubSection().remove(subsection);
//		sectionRepo.save(section);
		
	}

	@Transactional
	public boolean existById(Long subsectionid) {
		// TODO Auto-generated method stub
		return subRepo.existsById(subsectionid);
	}

}
