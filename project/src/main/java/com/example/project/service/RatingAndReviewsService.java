package com.example.project.service;



import com.example.project.service.Interface.CourseServiceInterface;
import com.example.project.service.Interface.RatingAndReviewsServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.Courses;
import com.example.project.entity.RatingAndReviews;
import com.example.project.entity.Users;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ResourceNotFoundException;


import com.example.project.repository.CourseRepository;
import com.example.project.repository.RatingAndReviewsRepository;
import com.example.project.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@Slf4j
public class RatingAndReviewsService implements RatingAndReviewsServiceInterface {


        @Autowired
	    private  RatingAndReviewsRepository ratingRepo;
        @Autowired
	    private UsersServiceInterface usersService;
        @Autowired
	    private CourseServiceInterface courseService;


	    
	    
	    
	    
	    
	@Transactional
	public void saveRatingAndReview(String rating, String review, String email, Long courseId) {
	   
		log.info("Attempting to save a rating for user {} on course ID {}", email, courseId);
        
	    Users user = usersService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("USer not found"));
	       

	    Courses course = courseService.findById(courseId);
	       // .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

	   
	    boolean isEnrolled = usersService.existsByUseridAndCoursesCourseid(user.getUserid(), courseId);
	    
	    if (!isEnrolled) {
	    	log.warn("Rating failed: User {} is not enrolled in course ID {}", email, courseId);
	           
	        throw new ResourceNotFoundException("Student is not enrolled in the course");
	    }
	    
	   
	    if (ratingRepo.existsByUserAndCourse(user, course)) {
	    	log.warn("Rating failed: User {} has already reviewed course ID {}", email, courseId);
            
	    	throw new ConflictException("User already reviewed this course");
	    }
	    
	    
	    RatingAndReviews rr = new RatingAndReviews();
	    rr.setRating(Integer.parseInt(rating));
	    rr.setReview(review);
	    
	    rr.setUser(user);
	    rr.setCourse(course); 
	    
	    ratingRepo.save(rr);
	    log.info("Rating and review saved successfully for user {} on course ID {}", email, courseId);
	    
	}

	

	

	
}
