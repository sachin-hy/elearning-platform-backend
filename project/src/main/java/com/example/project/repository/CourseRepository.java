package com.example.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.project.entity.Category;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;


@Repository
public interface CourseRepository extends JpaRepository<Courses,Long> {
	
	Page<Courses> findAll(Pageable pageable);

    Long countByCategory(Category category);

    @Query("Select c from Courses c where c.instructor = :instructor")
    List<Courses> findAllByInstructor(@Param("instructor") Users instructor);

	
}
