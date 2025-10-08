package com.example.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.dto.AllRatingResponseDto;
import com.example.project.entity.Courses;
import com.example.project.entity.RatingAndReviews;
import com.example.project.entity.Users;

@Repository
public interface RatingAndReviewsRepository extends JpaRepository<RatingAndReviews,Long> {

    boolean existsByUserAndCourse(Users user, Courses course);

}
