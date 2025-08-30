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

	@Query("SELECT r FROM RatingAndReviews r WHERE r.user = :user AND r.course = :course")
	Optional<RatingAndReviews> findByCourseAndUser(@Param("user") Users user,@Param("course") Courses course);

	@Query("SELECT COALESCE(AVG(r.rating), 0) FROM RatingAndReviews r WHERE r.course = :course" )
	Double getAverageRatingByCourse(Courses course);

	@Query("SELECT new com.example.project.dto.AllRatingResponseDto( r.review, r.rating, u.firstName, u.lastName, u.email, u.image, c.courseName) FROM RatingAndReviews r JOIN r.user u JOIN r.course c ORDER BY r.rating DESC")
	List<AllRatingResponseDto> findAllOrderByRatingDesc();

	List<RatingAndReviews> findByCourse(Courses course);

	boolean existsByUserAndCourse(Users user, Courses course);

}
