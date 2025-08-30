package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Courses;
import com.example.project.entity.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long> {

	@Modifying
	
	@Query("UPDATE Section s SET s.sectionName = :sectionName WHERE s.id = :sectionid")
	Section updateSectionNameById(@Param("sectionid") Long sectionid, @Param("sectionName") String sectionName);



	

}
