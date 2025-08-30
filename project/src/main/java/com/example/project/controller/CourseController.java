package com.example.project.controller;

import java.security.Principal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.dto.CourseDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Courses;
import com.example.project.entity.Section;
import com.example.project.entity.SubSection;
import com.example.project.entity.Category;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Users;
import com.example.project.service.CategoryService;
import com.example.project.service.ChatRoomService;
import com.example.project.service.CourseService;

import com.example.project.service.UsersService;
import com.example.project.utilis.ImageUploader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;




@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private ImageUploader imageUploader;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ChatRoomService chatRoomService;
	
	@PostMapping("/create-course")
	public ResponseEntity<?> createCourse( @ModelAttribute CourseDto courseDto,
			                                    Principal principal)
	{
		
	
			
			 MultipartFile file = courseDto.file();
			
			 if(file.isEmpty()) {
				  
			        return new ResponseEntity<>("File is empty!",HttpStatus.BAD_REQUEST);
			 }
			
		
			
			
			
			
			//check for instructor
			String email = principal.getName();
			
//			
			
			String thumbnailImage  = "";
			//upload Image
			try {
		      thumbnailImage = imageUploader.uploadFile(file);
			}catch(Exception e)
			{
				return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
			//create an entry for new course
			CourseResponseDto course = courseService.saveCourse(courseDto,email,thumbnailImage);
			
		 
			
			return new ResponseEntity<>(course,HttpStatus.OK);
			
		
		
	}
	
	
	@GetMapping("/courses-instructor")
	public ResponseEntity<?> getAllInstructorCourses(Principal principal )
	{
		
			System.out.println("get instructor course is called");
			String email = principal.getName();
			
			
			List<CourseResponseDto> listCourses =  courseService.findByInstructor(email); //usersService.findInstructorCourseByEmail(email);
			
			System.out.println("size of instructor courses = " + listCourses.size());
			return new ResponseEntity<>(listCourses,HttpStatus.OK);
			
		
	}
	
	@GetMapping("/courses-student")
	public ResponseEntity<?> getStudentCourses(Principal principal)
	{
		
			String email = principal.getName();
			
			
			List<CourseResponseDto> courses = usersService.findCoursesEnrolled(email);
			
			return new ResponseEntity<>(courses,HttpStatus.OK);
			
		
		
	}
	
	
	
	
	
//	@GetMapping("/categories/{type}/courses")
//	public ResponseEntity<?> getCourseByCategory(@PathVariable String type,@RequestParam("page")  String page)   
//	{
//		try {
//			
//			int p = Integer.parseInt(page);
//			Pageable pageable = PageRequest.of(p, 2);
//	        
//			
//			System.out.println("page number to excess the /getcoursesbycategory -------------------- =  " + p);
//			if(!type.equals("All"))
//			{
//				
//			    Category category = categoryService.getByName(type);
//			    List<CourseResponseDto> courseslist = null;
//			    if(category != null)
//			    {
//			    	courseslist = courseService.getByCategory(category,pageable);
//				    
//			    }else {
//			    	System.out.println("category is equals to null");
//			    }
//			    
//			      return new ResponseEntity<>(courseslist,HttpStatus.OK);
//			}
//			
//			else {
//				System.out.println("category All is called");
//				
//				List<CourseResponseDto> courseslist = courseService.getAll(pageable);
//				
//				return new ResponseEntity<>(courseslist,HttpStatus.OK);
//			} 
//		}catch(Exception e)
//		{
//			System.out.println("error = /getcoursesbycategory " + e);
//			return new ResponseEntity<>("Server Error! Please try Again",HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	
	               
	@GetMapping("/courses/size")  
	public ResponseEntity<?> getCourseListSize(@RequestParam("type") String type)
	{
		
		
		  Long ans =  courseService.getCourseSize(type);
			
			return new ResponseEntity<>(ans,HttpStatus.OK);
		
		
	}
	
	@DeleteMapping("/delete-course")
	public ResponseEntity<?> deleteCourse(@RequestParam("courseid") String cid)
	{
		
			
			courseService.deleteByid(cid);
			
			
			
			return new ResponseEntity<>("Course Deletetion successfull",HttpStatus.OK);
			
		
	}
 }

