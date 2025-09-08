package com.example.project.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Courses implements Serializable {
	
    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseid;
	
	private String courseName;
	
	private String courseDescription;
	
    private int price;
	
	private String thumbnail;
	
	private String whatyouwillLearn;
	
    private String status;
    
    private String tag;
    
    private String instructions;
	
	private LocalDateTime createdAt;
	
	@ManyToMany(mappedBy = "courses")
	@JsonIgnore
	private Set<Users> studentsEnrolled = new HashSet<>();
	
	
	@ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
	@JsonIgnore
	private Users  instructor;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
	@JsonIgnore
	private Category category;
	
	

	@OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
	@JsonManagedReference("course-content")
	private Set<Section> courseContent = new HashSet<>();
	
	
	
	@OneToMany(mappedBy = "course",cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("course-ratingandreviews")
	private Set<RatingAndReviews> ratingAndReviews = new HashSet<>();
	
	
	
	@OneToMany(mappedBy = "cartCourse",
			cascade = CascadeType.REMOVE, 
		    orphanRemoval = true)
	@JsonBackReference
	private Set<Cart> cart = new HashSet<>();
	
	
	@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "chatroom_id")
	private ChatRoom chatRoom;
	
	
	@OneToMany(mappedBy = "course")
	@JsonIgnore
	private Set<Orders> orders = new HashSet<>();
	
	
	public void removeChatRoomUser()
	{
		this.chatRoom.removeUser();
	}
	

	
	public void addSection(Section section)
	{
		this.courseContent.add(section);
		section.setCourse(this);
	}
	
	public void removeSection(Section section)
	{
		this.courseContent.remove(section);
		section.setCourse(null);
	}
	
	
	public void addInstructor(Users user)
	{
		this.instructor = user;
		user.getInstructorCourses().add(this);
	}
	
	public void removeInstructor()
	{
		this.instructor.getInstructorCourses().remove(this);
	    this.instructor = null;
	}
	
	
	public void removeStudentEnrolled()
	{
		for (Users user : new HashSet<>(this.studentsEnrolled)) {
		      user.removeCourse(this);  
		    }
	}
	
	
	
	public void addCategory(Category category)
	{
		this.category = category;
		category.getCourses().add(this);
	}
	
	public void removeCategory()
	{
		this.category.getCourses().remove(this);
	}

	
	public void addChatRoom(ChatRoom chatRoom)
	{
		this.chatRoom = chatRoom;
		chatRoom.setCourse(this);
	}
}
