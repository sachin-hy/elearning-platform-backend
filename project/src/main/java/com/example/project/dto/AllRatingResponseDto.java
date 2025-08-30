package com.example.project.dto;

import com.example.project.entity.RatingAndReviews;
import com.example.project.entity.Users;

public record AllRatingResponseDto(
	    String reviews,
	    int rating,
	    String firstName,
	    String lastName,
	    String email,
	    String image,
	    String courseName
	) {
	
	public AllRatingResponseDto(RatingAndReviews ratingandreviews)
	{
		this(ratingandreviews.getReview(),
				ratingandreviews.getRating(),
				ratingandreviews.getUser().getFirstName(),
				ratingandreviews.getUser().getLastName(),
				ratingandreviews.getUser().getEmail(),
				ratingandreviews.getUser().getImage(),
				ratingandreviews.getCourse().getCourseName());
	}
}

