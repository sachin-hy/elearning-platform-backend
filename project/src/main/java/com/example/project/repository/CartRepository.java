package com.example.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Cart;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long>{

    Page<Cart> findByUser(Users user, Pageable pageable);

	Cart findByUserAndCartCourse( Users user,  Courses course);

	int countByUser(Users user);
}
