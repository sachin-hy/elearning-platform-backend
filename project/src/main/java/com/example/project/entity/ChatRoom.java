package com.example.project.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String roomName;
	private String courseImageUrl;
	
	@OneToMany(mappedBy="chatRoom",cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Message> message = new HashSet<>();

	@ManyToMany(mappedBy = "chatRoom")
	private Set<Users> users = new HashSet<>();

	@OneToOne(mappedBy = "chatRoom")
	private Courses course;


	
	public void removeUser()
	{
	     for(Users u : new ArrayList<>(this.users))
	     {
	    	 u.getChatRoom().remove(this);
	     }
	     this.users = null;
	}

}
