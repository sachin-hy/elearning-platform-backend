package com.example.project.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project.entity.OTPSchema;

@Repository
public interface OtpSchemaRepository extends JpaRepository<OTPSchema,Long>{

	Optional<OTPSchema> findByotp(String otp);
    
	@Query(nativeQuery = true,value = "SELECT * FROM otpschema o WHERE o.email = :email AND o.expired_at > CURRENT_TIMESTAMP ORDER BY o.created_at DESC LIMIT 1 ")
	OTPSchema findByemail(String email);
}
