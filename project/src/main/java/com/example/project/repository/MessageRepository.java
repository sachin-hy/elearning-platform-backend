package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entity.ChatRoom;
import com.example.project.entity.Message;

public interface MessageRepository extends JpaRepository<Message,Long>{

	@Query("Select m from Message m where m.chatRoom = :chatRoom")
	List<Message> findMessageByChatRoom(@Param("chatRoom") ChatRoom chatRoom);

}
