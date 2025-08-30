package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.Section;
import com.example.project.entity.SubSection;

@Repository
public interface SubSectionRepository extends JpaRepository<SubSection,Long>{



	


}
