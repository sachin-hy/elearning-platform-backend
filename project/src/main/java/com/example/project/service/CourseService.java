package com.example.project.service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.project.service.Interface.CategoryServiceInterface;
import com.example.project.service.Interface.ChatRoomServiceInterface;
import com.example.project.service.Interface.CourseServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.apache.tika.Tika;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class CourseService implements CourseServiceInterface {

    @Autowired
	private  ImageUploader imageUploader;
    @Autowired
    private  CourseRepository courseRepo;
    @Autowired
    private UsersServiceInterface usersService;
    @Autowired
    private CategoryServiceInterface categoryService;
    @Autowired
    private  Tika tika;
    @Autowired
    private ChatRoomServiceInterface chatRoomService;
    
    
	
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
		Users instructor = usersService.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Instructor not found."));
                //userRepo.findByEmail(email)


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
		Category category = categoryService.findById(cid).orElseThrow(() ->  new ResourceNotFoundException("Enter valid Category Type"));

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
        course.addCategory(category);
        
        
        //save course
        Courses co = courseRepo.save(course);
        
        log.info("Course saved successfully with ID: {}", co.getCourseid());

		//save chatroom
		ChatRoom ch = chatRoomService.createChatRoom(courseDto.courseName(),thumbnailImage);

		//add instructor to chatrom
		instructor.addChatRoom(ch);
        
		//add chatroom of the course
	    co.addChatRoom(ch);
	    log.info("Chat room created and linked to course ID: {}", ch.getId());
        
     // return the response of the course
        return new CourseResponseDto(co);
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
	public List<CourseResponseDto> findByInstructor(String email) {
		
		log.info("Fetching courses for instructor with email: {}", email);
        
		Optional<Users> i = usersService.findByEmail(email);
		if(i.isEmpty())
		{
			log.warn("Instructor not found with email: {}", email);
            
			throw new ResourceNotFoundException("Enter a Valid Instructor Id");
		}

        Users instructor = i.get();

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
			Category category = categoryService.findByName(type).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

            return courseRepo.countByCategory(category);
		}
		
	}

    @Override
    public Courses findById(Long id) {
         Courses c = courseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No course Present With Id"));

          return c;
    }

    @Override
    public void save(Courses course) {
        courseRepo.save(course);
    }

    @Override
    public List<CourseResponseDto> findAllCourses(String page) {
        int p = 0;

        try {
            p = Integer.parseInt(page);
        }catch(NumberFormatException e)
        {

            throw new NumberFormatException("Enter Valid Page Number");
        }

        Pageable pageable = PageRequest.of(p, 2);

        Page<Courses> pa = courseRepo.findAll(pageable);

        return pa.map(course -> new CourseResponseDto(course)).getContent();
    }

}
