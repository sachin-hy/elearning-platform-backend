package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Profile;
import com.example.project.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long>{

	
	Users findByEmail(String email);

	
	String findPasswordByEmail(String email);

	@Modifying
	@Query("UPDATE Users u SET u.password = :newPassword WHERE u.email = :email")
	void updatePasswordByEmail(@Param("email") String email,@Param("newPassword") String newPassword);

	Users findByToken(String token);

	@Modifying
	@Query("UPDATE Users u SET u.additionalDetails = :additionalDetails WHERE u.email = :email")
	void updateProfileByEmail(@Param("email") String email, @Param("additionalDetails") Profile additionalDetails);

	void deleteByEmail(String email);

	@Modifying
	@Query("UPDATE Users u SET u.courses = :courses WHERE u.email = :email")
	void updateCoursesById(@Param("email") String email,@Param("courses") List<Courses> courses);


	@Query("SELECT u.instructorCourses FROM Users u WHERE u.email = :email")
	List<Courses> findInstructorCoursesByEmail(@Param("email") String email);
 
	@Query("SELECT u.courses FROM Users u WHERE u.email = :email")
	List<Courses> findCoursesEnrolled(@Param("email") String email);


	@Query("SELECT u.chatRoom FROM Users u WHERE u.email = :email")
	List<ChatRoom> findChatRoomsByEmail(@Param("email") String email);


	boolean existsByUseridAndCoursesCourseid(Long userid, Long courseId);

    @Query("SELECT EXISTS(SELECT 1 FROM Users WHERE email = :email)")
	boolean existByEmail(@Param("email") String email);


	boolean existsByIdAndCourses_Courseid(Long userid, long courseId);
	}
