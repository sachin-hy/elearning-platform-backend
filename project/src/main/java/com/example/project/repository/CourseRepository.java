package com.example.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Category;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;


@Repository
public interface CourseRepository extends JpaRepository<Courses,Long> {
	
	Page<Courses> findAll(Pageable pageable);

	@Query("SELECT DISTINCT u FROM Courses c JOIN c.studentsEnrolled u WHERE c.courseid = :courseId AND u = :user")
	Users isUserEnrolled(@Param("user") Users user, @Param("courseId") Long courseId);

	@Query("SELECT DISTINCT c FROM Courses c " +
		       "JOIN c.studentsEnrolled u " +
		       "WHERE c.courseid = :courseId AND u = :user")
	Courses findCourseByEnrolledUser(
		    @Param("user") Users user, 
		    @Param("courseId") Long courseId
		);
	
	Page<Courses> findByCategory(Category category, Pageable pageable);

	Long countByCategory(Category category);

	@Query("select c.chatRoom from Courses c where c.courseid = :courseId")
	ChatRoom findChatRoomById(@Param("courseId") Long courseId);

	@Query("Select c from Courses c where c.instructor = :instructor")
	List<Courses> findAllByInstructor(@Param("instructor") Users instructor);

	

	
	
}
