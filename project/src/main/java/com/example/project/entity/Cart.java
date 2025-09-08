package com.example.project.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", unique = false)
	@JsonIgnore
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "course_id", unique = false)
	@JsonManagedReference
	private Courses cartCourse;
	
	
	
	public void addCourse(Courses course)
	{
		this.cartCourse = course;
		course.getCart().add(this);
	}
	
	public void addUser(Users user)
	{
		this.user = user;
		user.getCartCourses().add(this);
	}
	
}





