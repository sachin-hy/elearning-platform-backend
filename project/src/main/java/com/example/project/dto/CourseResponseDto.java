package com.example.project.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.project.entity.Courses;
import com.example.project.entity.RatingAndReviews;
import com.example.project.entity.Section;
import com.example.project.entity.Users;

public record CourseResponseDto(
		    Long courseid,
		    int price,
		    String courseName,
		    String thumbnail,
		    String courseDescription,
		    List<SectionResponseDto> courseContent,
		    String whatyouwillLearn,
		    String instructor,
		    List<AllRatingResponseDto> ratingAndReviews
		   
		) {
	
	   public CourseResponseDto(Courses course)
	   {
		   this(course.getCourseid(),
				   course.getPrice(),
				   course.getCourseName(),
				   course.getThumbnail(),
				   course.getCourseDescription(),
				   course.getCourseContent() != null ?
				   course.getCourseContent().stream()
				   .map(section -> new SectionResponseDto(section))
				   .collect(Collectors.toList())
				   :
				   new ArrayList<SectionResponseDto>(),
				   course.getWhatyouwillLearn(),
				   course.getInstructor().getFullName(),
				   course.getRatingAndReviews() != null ?
				   course.getRatingAndReviews().stream()
				   .map(ratingandreviews -> new AllRatingResponseDto(ratingandreviews))
				   .collect(Collectors.toList())
				   :
				    new ArrayList<AllRatingResponseDto>()
				   );
	   }
	 
}
