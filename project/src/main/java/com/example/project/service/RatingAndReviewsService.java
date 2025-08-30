package com.example.project.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.AllRatingResponseDto;
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

	
	@Autowired
	private RatingAndReviewsRepository ratingRepo;
    
	
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	
//	@Transactional
//	public boolean checkUserReviewed(Users user, Courses course) {
//		
//
//		Optional<RatingAndReviews> ratingandreviews = ratingRepo.findByCourseAndUser(user,course);
//		
//		
//		if(ratingandreviews.isPresent())
//		{
//			return true;
//		}
//		else
//		{
//			System.out.println("***********************************");
//			System.out.println("USernot found");
//			return false;
//		}
//	}
//	
	
	@Transactional
	public void saveRatingAndReview(String rating, String review, String email, Long courseId) {
	    // 1. Safe retrieval of entities from the database
	    Users user = userRepo.findByEmail(email);
	        //.orElseThrow(() -> new UserNotFoundException("User not found"));
	    
	    Courses course = courseRepo.findById(courseId)
	        .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

	    // 2. Efficiently check if the user is enrolled (via a single database query)
	    boolean isEnrolled = userRepo.existsByUseridAndCoursesCourseid(user.getUserid(), courseId);
	    
	    if (!isEnrolled) {
	        throw new ResourceNotFoundException("Student is not enrolled in the course");
	    }
	    
	    // 3. Efficiently check if the user has already reviewed the course
	    if (ratingRepo.existsByUserAndCourse(user, course)) {
	        throw new ConflictException("User already reviewed this course");
	    }
	    
	    // 4. Create and save the new review
	    RatingAndReviews rr = new RatingAndReviews();
	    rr.setRating(Integer.parseInt(rating));
	    rr.setReview(review);
	    
	    rr.setUser(user); // Assuming you have a setUser method
	    rr.setCourse(course); // Assuming you have a setCourse method
	    
	    ratingRepo.save(rr);
	}

	

	

	
}
