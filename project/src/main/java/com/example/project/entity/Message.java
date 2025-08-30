package com.example.project.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String senderName;
	private String content;
	private String senderImageUrl;
	private LocalDateTime time;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Users user;
	
	@ManyToOne
	ChatRoom chatRoom;

	public void addUser(Users user)
	{
		this.user = user;
		user.getMessage().add(this);
	}
	
	public void addChatRoom(ChatRoom chatRoom)
	{
	    this.chatRoom = chatRoom;
	    chatRoom.getMessage().add(this);
	}
	
}
