package com.example.project.service.Interface;

import com.example.project.dto.CourseResponseDto;

import java.util.List;

public interface CartServiceInterface {

    public List<CourseResponseDto> findcartCourseByUser(String email , String page);

    public CourseResponseDto save(String email,String courseid);
    public int findcartSizeByUser(String email);



}
