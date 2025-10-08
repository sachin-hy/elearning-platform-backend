package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.project.entity.SubSection;

@Repository
public interface SubSectionRepository extends JpaRepository<SubSection,Long>{

}
