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

@Service
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
	   
	    Users user = userRepo.findByEmail(email);
	       
	    
	    Courses course = courseRepo.findById(courseId)
	        .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

	   
	    boolean isEnrolled = userRepo.existsByUseridAndCoursesCourseid(user.getUserid(), courseId);
	    
	    if (!isEnrolled) {
	        throw new ResourceNotFoundException("Student is not enrolled in the course");
	    }
	    
	   
	    if (ratingRepo.existsByUserAndCourse(user, course)) {
	        throw new ConflictException("User already reviewed this course");
	    }
	    
	    
	    RatingAndReviews rr = new RatingAndReviews();
	    rr.setRating(Integer.parseInt(rating));
	    rr.setReview(review);
	    
	    rr.setUser(user);
	    rr.setCourse(course); 
	    
	    ratingRepo.save(rr);
	}

	

	

	
}
