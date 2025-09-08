package com.example.project.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {



	
	
}
