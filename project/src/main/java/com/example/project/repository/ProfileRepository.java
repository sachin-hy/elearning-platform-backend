package com.example.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Profile;
import com.example.project.entity.Users;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {



	
	
}
