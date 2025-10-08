package com.example.project.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.project.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;
	private String firstName;
	private String lastName;
	private String email;	private String password;
    @Enumerated(EnumType.STRING)
	private AccountType accountType;
	private Boolean active;
	private Boolean approve;
	
	private String image;
	private String token;
	private LocalDateTime resetPasswordExpires;
	
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_enrolled_courses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
	private Set<Courses> courses = new HashSet<>();
	
	
	
	@OneToMany(mappedBy = "instructor", 
            cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Courses> instructorCourses = new HashSet<>();

	
	
	@OneToOne(cascade = CascadeType.ALL, 
            orphanRemoval = true)
    @JoinColumn(name = "profile_id")
	private Profile additionalDetails;
	

	@OneToMany(cascade = CascadeType.REMOVE,
			  orphanRemoval = true,
			  mappedBy = "user")
	@JsonIgnore
	private Set<Cart> cartCourses = new HashSet<>();
	
	
	
	@ManyToMany
	@JoinTable(
			name="user_chatroom",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "chatroom_id"))
	private Set<ChatRoom> chatRoom = new HashSet<>();
	
	
	
	@OneToMany(mappedBy= "user" ,cascade = CascadeType.REMOVE,orphanRemoval = true)
	@JsonIgnore
	private Set<Message> message =new HashSet<>();
	
	
	@OneToMany(mappedBy="user", cascade = CascadeType.REMOVE,orphanRemoval = true)
	@JsonIgnore
	private Set<RatingAndReviews> ratingAndReviews = new HashSet<>();
	
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<Orders> userOrder = new HashSet<>();
	

	public String getFullName() {
        return firstName + " " + lastName;
    }

	public void addCourse(Courses c)
	{
		 this.courses.add(c);
		 c.getStudentsEnrolled().add(this);
	}
	
	
	public void removeCourse(Courses c)
	{
		this.courses.remove(c);
		c.getStudentsEnrolled().remove(this);
	}
	
	public void addChatRoom(ChatRoom chatRoom)
	{
		this.getChatRoom().add(chatRoom);
		chatRoom.getUsers().add(this);
	}
	
	
}
