package com.example.project.service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tika.Tika;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.dto.CourseDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Category;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.exception.UnsupportedFileTypeException;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ChatRoomRepository;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.UsersRepository;
import com.example.project.utilis.ImageUploader;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {
	
	
	private final ImageUploader imageUploader;
    private final CourseRepository courseRepo;
    private final UsersRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ChatRoomRepository chatRoomRepo;
    private final Tika tika;

    public CourseService(ImageUploader imageUploader, CourseRepository courseRepo, UsersRepository userRepo,
                         CategoryRepository categoryRepo, ChatRoomRepository chatRoomRepo, Tika tika) {
        this.imageUploader = imageUploader;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.chatRoomRepo = chatRoomRepo;
        this.tika = tika;
    }
    
    
    
    
	
	@Transactional
	public CourseResponseDto saveCourse(CourseDto courseDto, String email) throws IOException {
		
		log.info("Attempting to save new course: {} for user: {}", courseDto.courseName(), email);

		
		MultipartFile file = courseDto.file();
		
		if(file.isEmpty())
		{
			log.warn("File upload failed: file is empty for user: {}", email);
	           
			throw new ResourceNotFoundException("Select the File to Upload");
		}
		
		
		String type = tika.detect(file.getInputStream());
			
	    if(!"image/jpeg".equals(type) && !"image/png".equals(type))
	    {
	    	log.warn("Invalid file type detected: {}", type);
	           
	    	throw new UnsupportedFileTypeException("Invalid file type: only JPEG and PNG images are allowed.");
	    }
		
		//Upload FIle to Cloudnary 
	    String thumbnailImage  = imageUploader.uploadFile(file);;
	    log.info("File successfully uploaded to Cloudinary: {}", thumbnailImage);

	   //Get Instrucotre
		Users instructor = userRepo.findByEmail(email);
		if (instructor == null) {
            log.warn("Instructor not found with email: {}", email);
            throw new ResourceNotFoundException("Instructor not found.");
        }
		//check valid course id
		Long cid =0l;
		try {
			cid = Long.parseLong(courseDto.category());
		}catch(NumberFormatException e)
		
		{
			 log.warn("Invalid category ID format: {}", courseDto.category());
	           
			throw new NumberFormatException("Enter Valid Category");
		}
		
		//get category of the course using the category id
		Category c = categoryRepo.findById(cid).get();
		
		
		if(c == null)
		{
			throw new ResourceNotFoundException("Enter valid Category Type");
		}
		
		//create new course
		Courses course =new Courses();
		course.setCourseName(courseDto.courseName());
		course.setCourseDescription(courseDto.courseDescription());
		course.setWhatyouwillLearn(courseDto.whatyouwillLearn());
		course.setPrice(courseDto.price());
        course.setThumbnail(thumbnailImage);
        course.setCreatedAt(LocalDateTime.now());
        course.setTag(courseDto.tag());
        
        course.addInstructor(instructor);
        course.addCategory(c);
        
        
        //save course
        Courses co = courseRepo.save(course);
        
        log.info("Course saved successfully with ID: {}", co.getCourseid());

        //create chatroom for the course
        ChatRoom chatRoom = new ChatRoom();
		chatRoom.setRoomName(courseDto.courseName());
		chatRoom.setCourseImageUrl(thumbnailImage);
	   
		//save chatroom
		ChatRoom ch = chatRoomRepo.save(chatRoom);
		
		//add instructor to chatrom
		instructor.addChatRoom(ch);
        
		//add chatroom of the course
	    co.addChatRoom(ch);
	    log.info("Chat room created and linked to course ID: {}", ch.getId());
        
     // return the response of the course
        return new CourseResponseDto(co);
	}



	

	
	@Transactional
	public CourseResponseDto getCourseById(Long courseid) {
		// TODO Auto-generated method stub
		log.info("Fetching course by ID: {}", courseid);
        
		 Courses course = courseRepo.findById(courseid).get();
		 return new CourseResponseDto(course);
	}

	@Transactional
	public Courses findById(Long courseid)
	{
		 log.info("Finding course by ID: {}", courseid);
	       
		return courseRepo.findById(courseid).get();
	}
	
	
	@Transactional
	public boolean getUserEnrolled(Users user,Long courseid) {
		log.info("Checking if user {} is enrolled in course ID: {}", user.getEmail(), courseid);
        
	
		Users u =  courseRepo.isUserEnrolled(user,courseid);
		
		if(u == null)
		{
			return false;
		}
		else {
			return true;
		}
		
	}

	@Transactional
	public Courses getCourseAndCheckUserEnrolled(Users user, Long courseid) {
		// TODO Auto-generated method stub
		log.info("Fetching course ID {} and checking enrollment for user {}", courseid, user.getEmail());
        
		return courseRepo.findCourseByEnrolledUser(user,courseid);
	}


   
	 
	@Transactional
	public List<CourseResponseDto> getByCategory(Category category,Pageable pageable) {
		 log.info("Fetching courses by category: {} on page {}", category.getName(), pageable.getPageNumber());
	       
		
		Page<Courses> page = courseRepo.findByCategory(category, pageable);
		
		log.info("Found {} courses in category {}.", page.getTotalElements(), category.getName());
	       
		return convert(page);
	}


	@Transactional
	public List<CourseResponseDto> getAll(Pageable pageable) {
		// TODO Auto-generated method stub
	    
		 log.info("Fetching all courses on page {}", pageable.getPageNumber());
	       
		Page<Courses> page = courseRepo.findAll(pageable);
		 log.info("Found {} courses in total.", page.getTotalElements());
	       
	     return convert(page);  
	}





	@Transactional
	public void deleteByid(String cid) {
		
		 log.info("Attempting to delete course with ID: {}", cid);
	      
		  Long courseid = 0l;
		  try {
			  courseid = Long.parseLong(cid);
		  }catch(NumberFormatException e)
		  {
			  log.warn("Invalid course ID format for deletion: {}", cid);
	           
			  throw new NumberFormatException("Enter zvalid Course Id");
		  }
		
		  if(!courseRepo.existsById(courseid))
		  {
			 throw new ResourceNotFoundException("No Course Exist With the Id"); 
		  }
		  
		  
		  Courses c = courseRepo.findById(courseid).get();

		  if (!c.getOrders().isEmpty()) {
			  log.warn("Cannot delete course ID {} because it has existing orders.", courseid);
	           
		        throw new ConflictException("Cannot delete a course that has existing orders. Please archive it instead.");
		   }
		  
		  c.removeInstructor();
		  c.removeCategory();
		  c.removeStudentEnrolled();
		  c.removeChatRoomUser();
		  
		  
		  courseRepo.deleteById(courseid);
		  log.info("Course with ID {} and its associations successfully deleted.", courseid);
		  
		
	}





	@Transactional
	public List<CourseResponseDto> convert(Page<Courses> page)
	{
		 return page.map(course -> new CourseResponseDto(course)).getContent();
	}








 
	@Transactional
	public List<CourseResponseDto> findByInstructor(String email) {
		
		log.info("Fetching courses for instructor with email: {}", email);
        
		Users instructor = userRepo.findByEmail(email);
		if(instructor == null)
		{
			log.warn("Instructor not found with email: {}", email);
            
			throw new ResourceNotFoundException("Enter a Valid Instructor Id");
		}
		
		List<Courses> list = courseRepo.findAllByInstructor(instructor);
		
		log.info("Found {} courses for instructor {}.", list.size(), email);
        
		return list.stream().map(course -> new CourseResponseDto(course)).collect(Collectors.toList());
	}






	@Transactional
	public Long getCourseSize(String type) {
		// TODO Auto-generated method stub
		log.info("Getting course size for type: {}", type);
	       
		if(type.equals("All"))
		{
		    return courseRepo.count();
		}else {
			Category category = categoryRepo.findByName(type);
			return courseRepo.countByCategory(category);
		}
		
	}
	

}
