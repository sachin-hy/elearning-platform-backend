package com.example.project.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.AllRatingResponseDto;

import com.example.project.entity.Courses;
import com.example.project.entity.RatingAndReviews;
import com.example.project.entity.Users;
import com.example.project.service.CourseService;
import com.example.project.service.RatingAndReviewsService;
import com.example.project.service.UsersService;

@RestController
@RequestMapping("/rating-and-review")
public class RatingAndReviewController {

	@Autowired
	private RatingAndReviewsService ratingService;
	
	@Autowired
	private CourseService courseService;
	@Autowired
	private UsersService usersService;
	
	
	
	//create Rating
	@PostMapping("/create-rating")
	public ResponseEntity<String> createRating(@RequestParam("rating") String rating,
			                                   @RequestParam("review") String review,
			                                   @RequestParam("courseid") String cid,
			                                  Principal principal)
	{
			//get userid
			String email = principal.getName();
			
			//courseid
			Long courseid = Long.parseLong(cid);
			
			
			ratingService.saveRatingAndReview(rating,review,email,courseid);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		
	}
	
	
	
}
