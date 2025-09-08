package com.example.project.controller;

import java.io.IOException;
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
import com.example.project.service.CourseService;

import com.example.project.service.UsersService;

import jakarta.validation.Valid;





@RestController
@RequestMapping("/course")
public class CourseController {


	private final UsersService usersService;
	private final CourseService courseService;
	
	
	public CourseController( UsersService usersService,CourseService courseService)
	{
		this.usersService = usersService;
		this.courseService = courseService;
	}
	
	
	
	@PostMapping("/create-course")
	public ResponseEntity<?> createCourse(@Valid @ModelAttribute CourseDto courseDto,
			                                    Principal principal) throws IOException
	{
		
			//check for instructor
			String email = principal.getName();
			//create an entry for new course
			CourseResponseDto course = courseService.saveCourse(courseDto,email);
			
		 
			
			return new ResponseEntity<>(course,HttpStatus.OK);
			
		
		
	}
	
	
	@GetMapping("/courses-instructor")
	public ResponseEntity<?> getAllInstructorCourses(Principal principal )
	{
		
			
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

