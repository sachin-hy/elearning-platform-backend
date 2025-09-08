package com.example.project.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.project.service.RatingAndReviewsService;

@RestController
@RequestMapping("/rating-and-review")
public class RatingAndReviewController {

	private final RatingAndReviewsService ratingService;

    public RatingAndReviewController(RatingAndReviewsService ratingService) {
        this.ratingService = ratingService;
    }

	
	
	
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
