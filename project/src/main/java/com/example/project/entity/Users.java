package com.example.project.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.PreRemove;


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
	private String email;
	private String password;
	private String accountType;
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
	
	
	public enum AccountType {
	    ADMIN, STUDENT, INSTRUCTOR
	}
	
	
	public String getFullName() {
        return firstName + " " + lastName;
    }
	
	
	
//	
//	
//	@PreRemove
//	private void preRemove() {
////	    // Remove user from all enrolled courses
////	    if (courses != null) {
////	        for (Courses course : new ArrayList<>(courses)) {
////	            course.getStudentsEnrolled().remove(this);
////	        }
////	        courses.clear();
////	    }
//
//	    // Remove user from all chatrooms
//	    if (chatRoom != null) {
//	        for (ChatRoom room : new ArrayList<>(chatRoom)) {
//	            room.getUsers().remove(this);
//	        }
//	        chatRoom.clear();
//	    }
//
//	    // Remove cart entries (already cascaded, optional)
//	    if (cartCourses != null) {
//	        for (Cart cart : new ArrayList<>(cartCourses)) {
//	            cart.setUser(null);  // break FK
//	        }
//	        cartCourses.clear();
//	    }
//	}

	
	
	
	
//	public void enrollInCourse(Courses course)
//	{
//		courses.add(course);
//		course.getStudentsEnrolled().add(this);
//	}
//	
//	public void unenrollFromCourse(Courses course) {
//        courses.remove(course);
//        course.getStudentsEnrolled().remove(this);
//    }
	
	
	
	
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
