package com.example.project.service.Interface;

import com.example.project.dto.CourseResponseDto;
import com.example.project.dto.LoginDto;
import com.example.project.dto.LoginResponseDto;
import com.example.project.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Optional;

public interface UsersServiceInterface {
    public Optional<Users> findByEmail(String email);
    public String getPassword(String email);
    public String updatePassword(String email, String newPassword);
    public Users findBytoken(String token);

    public List<CourseResponseDto> findCoursesEnrolled(String email);
    public void setUserToken(String email,String token);
    public LoginResponseDto checkCredentials(LoginDto logindto, HttpServletRequest request);


    public boolean existsByUseridAndCourses_Courseid(Long userid, long courseId);

    public boolean existsByUseridAndCoursesCourseid(Long userid, Long courseId);

    public boolean existByEmail(@Email(message = "Enter  a valid Emial Address") String email);

    public void save(Users user);
}
