package com.example.project.service;



import com.example.project.service.Interface.SectionServiceInterface;
import com.example.project.service.Interface.SubSectionServiceInterface;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.dto.SubSectionDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.entity.Section;
import com.example.project.entity.SubSection;
import com.example.project.exception.BadRequestException;
import com.example.project.exception.InternalServerError;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.exception.UnsupportedFileTypeException;
import com.example.project.repository.SectionRepository;
import com.example.project.repository.SubSectionRepository;
import com.example.project.utilis.ImageUploader;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubSectionService implements SubSectionServiceInterface {

    @Autowired
	private  SubSectionRepository subRepo;
    @Autowired
    private SectionServiceInterface sectionService;
    @Autowired
    private  ImageUploader imageUploader;
    @Autowired
    private  Tika tika;


	@Transactional
    @CacheEvict(value = {"coursesByCategory","allCourses","courseInstructor","courseInstructor"}, allEntries = true, beforeInvocation = false)
	public SubSectionResponseDto saveSubSection(SubSectionDto sectionDto) {
		log.info("Attempting to save a new sub-section with title: {}", sectionDto.title());

		if(!sectionService.existsById(sectionDto.sectionid()))
		{
			throw new ResourceNotFoundException("Enter Valid Section Id");
		}

		MultipartFile file = sectionDto.vedioUrl();
		if (file == null || file.isEmpty()) {
			log.warn("Video upload failed: file is empty for section ID: {}");
			throw new BadRequestException("Please select a video file to upload.");
		}

		// check the file is not corrupted and get its type
		String type="";
		try {
			type = tika.detect(file.getInputStream());
			log.info("Detected file type: {}", type);
		}catch(Exception e)
		{
			log.error("Failed to detect file type for uploaded video.", e);

			throw new UnsupportedFileTypeException("Could not read or process the uploaded file. It may be corrupted.");
		}


		//chack the type of file
		if(!"video/mp4".equals(type) && !"video/wmv".equals(type))
        {
			 log.warn("Unsupported file type: {}", type);

	        throw new UnsupportedFileTypeException("Invalid file type: only mp4 and wmv file Type Allowed.");
        }

		// uplodae file to cloudnary and get the url
		String vedioUrl = "";
		try {
		 vedioUrl = imageUploader.uploadFile(sectionDto.vedioUrl());

		 log.info("Video successfully uploaded to Cloudinary: {}", vedioUrl);

		}catch(Exception e)
		{
			log.error("Failed to upload video to Cloudinary.", e);

			throw new InternalServerError("Server Error Please Try After Some Time");
		}

		//find section
		Section section = sectionService.findById(sectionDto.sectionid())
                .orElseThrow(() -> new ResourceNotFoundException("Section Id: " + sectionDto.sectionid()));

		//create new subsection
		SubSection subsection = new SubSection();
		subsection.setTitle(sectionDto.title());
		subsection.setTimeDuration(sectionDto.timeDuration());
		subsection.setDescription(sectionDto.description());
		subsection.setVedioUrl(vedioUrl);

		subsection.addSection(section);

        sectionService.save(section);
		log.info("Sub-section saved successfully with title: {}", subsection.getTitle());

		return new SubSectionResponseDto(subsection);

	}




	@Transactional
    @CacheEvict(value = {"coursesByCategory","allCourses","courseInstructor","courseInstructor"}, allEntries = true, beforeInvocation = false)
	public SubSectionResponseDto findByIdAndUpdate(SubSectionDto subsectionDto) {
		log.info("Attempting to update sub-section with ID: {}", subsectionDto.subsectionid());

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
			log.error("Failed to upload video during sub-section update for ID: {}", subsectionid, e);

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
		log.info("Sub-section with ID: {} updated successfully.", subsectionid);


		return new SubSectionResponseDto(subsection);

	}



	@Transactional
    @CacheEvict(value = {"coursesByCategory","allCourses","courseInstructor","courseInstructor"}, allEntries = true, beforeInvocation = false)
	public void deleteById(String id) {
		log.info("Attempting to delete sub-section with ID: {}", id);

		Long subsectionid = 0l;
		try {
			subsectionid = Long.parseLong(id);
		}catch(Exception e)
		{
			log.warn("Invalid sub-section ID format for deletion: {}", id);

			throw new NumberFormatException("Enter Valid Subsection Id");
		}

		if(!subRepo.existsById(subsectionid))
		{
			throw new ResourceNotFoundException("Enter Valid Subsection Id");
		}


		SubSection subsection = subRepo.findById(subsectionid).get();
		subsection.removeSection();

		subRepo.deleteById(subsectionid);

		log.info("Sub-section with ID: {} deleted successfully.", subsectionid);


	}

}
