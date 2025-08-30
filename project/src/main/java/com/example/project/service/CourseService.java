package com.example.project.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.AllRatingResponseDto;
import com.example.project.dto.CourseDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.dto.SectionResponseDto;
import com.example.project.dto.SubSectionResponseDto;
import com.example.project.entity.Category;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Section;
import com.example.project.entity.Users;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ChatRoomRepository;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.UsersRepository;

@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private UsersRepository userRepo;

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ChatRoomRepository chatRoomRepo;
	
	
	@Transactional
	public CourseResponseDto saveCourse(CourseDto courseDto, String email,  String thumbnailImage) {
		Users instructor = userRepo.findByEmail(email);
		Long cid =0l;
		try {
			cid = Long.parseLong(courseDto.category());
		}catch(NumberFormatException e)
		
		{
			throw new NumberFormatException("Enter Valid Category");
		}
		
		Category c = categoryRepo.findById(cid).get();
		
		
		if(c == null)
		{
			throw new ResourceNotFoundException("Enter valid Category Type");
		}
		
		
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
        Courses co = courseRepo.save(course);
        
        ChatRoom chatRoom = new ChatRoom();
		chatRoom.setRoomName(courseDto.courseName());
		chatRoom.setCourseImageUrl(thumbnailImage);
	   
		ChatRoom ch = chatRoomRepo.save(chatRoom);
		
		instructor.addChatRoom(ch);
//		chatRoom.getUsers().add(instructor);
//	    instructor.getChatRoom().add(chatRoom);
	    co.addChatRoom(ch);
		
//	    chatRoom.setCourse(course);
		
        
        
        return new CourseResponseDto(co);
	}



	

	
	@Transactional
	public CourseResponseDto getCourseById(Long courseid) {
		// TODO Auto-generated method stub
		 Courses course = courseRepo.findById(courseid).get();
		 return new CourseResponseDto(course);
	}

	@Transactional
	public Courses findById(Long courseid)
	{
		return courseRepo.findById(courseid).get();
	}
	
	
	@Transactional
	public boolean getUserEnrolled(Users user,Long courseid) {
		
	
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

		return courseRepo.findCourseByEnrolledUser(user,courseid);
	}


   
	 
	@Transactional
	public List<CourseResponseDto> getByCategory(Category category,Pageable pageable) {
		Page<Courses> page = courseRepo.findByCategory(category, pageable);
		return convert(page);
	}


	@Transactional
	public List<CourseResponseDto> getAll(Pageable pageable) {
		// TODO Auto-generated method stub
	     Page<Courses> page = courseRepo.findAll(pageable);
	     
	     return convert(page);  
	}





	@Transactional
	public void deleteByid(String cid) {
		
		  Long courseid = 0l;
		  try {
			  courseid = Long.parseLong(cid);
		  }catch(NumberFormatException e)
		  {
			  throw new NumberFormatException("Enter zvalid Course Id");
		  }
		
		  if(!courseRepo.existsById(courseid))
		  {
			 throw new ResourceNotFoundException("No Course Exist With the Id"); 
		  }
		  
		  
		  Courses c = courseRepo.findById(courseid).get();

		  if (!c.getOrders().isEmpty()) {
		        throw new ConflictException("Cannot delete a course that has existing orders. Please archive it instead.");
		   }
		  
		  c.removeInstructor();
		  c.removeCategory();
		  c.removeStudentEnrolled();
		  c.removeChatRoomUser();
		  
		  
		  courseRepo.deleteById(courseid);
		
	}





	@Transactional
	public List<CourseResponseDto> convert(Page<Courses> page)
	{
		 return page.map(course -> new CourseResponseDto(course)).getContent();
	}






//	@Transactional
//	public boolean checkCourseById(Long courseid) {
//		// TODO Auto-generated method stub
//		return 
//	}





 
	@Transactional
	public List<CourseResponseDto> findByInstructor(String email) {
		Users instructor = userRepo.findByEmail(email);
		if(instructor == null)
		{
			throw new ResourceNotFoundException("Enter a Valid Instructor Id");
		}
		
		List<Courses> list = courseRepo.findAllByInstructor(instructor);
		return list.stream().map(course -> new CourseResponseDto(course)).collect(Collectors.toList());
	}






	@Transactional
	public Long getCourseSize(String type) {
		// TODO Auto-generated method stub
		if(type.equals("All"))
		{
		    return courseRepo.count();
		}else {
			Category category = categoryRepo.findByName(type);
			return courseRepo.countByCategory(category);
		}
		
	}
	

}
