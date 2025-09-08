package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RatingAndReviews {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int rating;
	
	private String review;
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	@JsonBackReference("course-ratingandreviews")
	private Courses course;
	 
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference("user-ratingandreviews")
	private Users user;
	
	
	public void addUser(Users user)
	{
		this.user = user;
		user.getRatingAndReviews().add(this);
	}
	
	
	public void addCourse(Courses course)
	{
		this.course = course;
		course.getRatingAndReviews().add(this);
	}
	

	
}
