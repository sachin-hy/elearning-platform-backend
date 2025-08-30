package com.example.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Category;
import com.example.project.entity.Courses;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
	Category findByName(String category);

	@Query("SELECT DISTINCT c FROM Category c WHERE c.id != :cid")
	List<Category> findNotById(@Param("cid") Long cid);

	@Query("Select c.courses from Category c Where c.name = :type")
	Page<Courses> getCourseByCategory(@Param("type") String type, Pageable pageable);


}
