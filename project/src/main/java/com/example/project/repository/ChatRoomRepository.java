package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.ChatRoom;
import com.example.project.entity.Message;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

	@Query("SELECT c.message FROM ChatRoom c WHERE c.id = :roomId")
	List<Message> findMessageById(@Param("roomId") Long roomId);

	@Query("Select c from ChatRoom c JOIN c.users u where u.email = :email")
	List<ChatRoom> findChatRoomsByEmail(@Param("email") String email);


}
