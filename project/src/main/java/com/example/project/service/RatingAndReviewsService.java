package com.example.project.service;



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

@Service
@Slf4j
public class RatingAndReviewsService {

	
	 private final RatingAndReviewsRepository ratingRepo;
	    private final UsersRepository userRepo;
	    private final CourseRepository courseRepo;

	    public RatingAndReviewsService(RatingAndReviewsRepository ratingRepo, UsersRepository userRepo, CourseRepository courseRepo) {
	        this.ratingRepo = ratingRepo;
	        this.userRepo = userRepo;
	        this.courseRepo = courseRepo;
	    }
	
	    
	    
	    
	    
	    
	@Transactional
	public void saveRatingAndReview(String rating, String review, String email, Long courseId) {
	   
		log.info("Attempting to save a rating for user {} on course ID {}", email, courseId);
        
	    Users user = userRepo.findByEmail(email);
	       
	    if (user == null) {
            log.warn("Rating failed: User not found with email: {}", email);
            throw new ResourceNotFoundException("User not found.");
        }
	    Courses course = courseRepo.findById(courseId)
	        .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

	   
	    boolean isEnrolled = userRepo.existsByUseridAndCoursesCourseid(user.getUserid(), courseId);
	    
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
