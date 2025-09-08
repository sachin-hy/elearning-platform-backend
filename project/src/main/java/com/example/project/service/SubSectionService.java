package com.example.project.service;



import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.dto.SubSectionDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.entity.Section;
import com.example.project.entity.SubSection;
import com.example.project.exception.InternalServerError;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.exception.UnsupportedFileTypeException;
import com.example.project.repository.SectionRepository;
import com.example.project.repository.SubSectionRepository;
import com.example.project.utilis.ImageUploader;

@Service
public class SubSectionService {
	
	private final SubSectionRepository subRepo;
    private final SectionRepository sectionRepo;
    private final ImageUploader imageUploader;
    private final Tika tika;

    public SubSectionService(SubSectionRepository subRepo, SectionRepository sectionRepo, ImageUploader imageUploader, Tika tika) {
        this.subRepo = subRepo;
        this.sectionRepo = sectionRepo;
        this.imageUploader = imageUploader;
        this.tika = tika;
    }
	
	@Transactional
	public SubSectionResponseDto saveSubSection(SubSectionDto sectionDto) {
		
		if(!sectionRepo.existsById(sectionDto.sectionid()))
		{
			throw new ResourceNotFoundException("Enter Valid Section Id");
		}
		
		MultipartFile file = sectionDto.vedioUrl();
		
		
		// check the file is not corrupted and get its type
		String type="";
		try {
			type = tika.detect(file.getInputStream());
		}catch(Exception e)
		{
			throw new UnsupportedFileTypeException("Could not read or process the uploaded file. It may be corrupted.");
		}
		
		
		//chack the type of file
		if(!"video/mp4".equals(type) && !"video/wmv".equals(type))
        { 
	        throw new UnsupportedFileTypeException("Invalid file type: only mp4 and wmv video are allowed.");
        }
		
		// uplodae file to cloudnary and get the url
		String vedioUrl = "";
		try {
		 vedioUrl = imageUploader.uploadFile(sectionDto.vedioUrl());
		}catch(Exception e)
		{
			throw new InternalServerError("Server Error Please Try After Some Time");
		}
		
		//find section
		Section section = sectionRepo.findById(sectionDto.sectionid()).get();
		
		//create new subsection
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
		

		
	}

	@Transactional
	public boolean existById(Long subsectionid) {
		// TODO Auto-generated method stub
		return subRepo.existsById(subsectionid);
	}

}
