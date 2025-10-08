package com.example.project.service.Interface;

import com.example.project.dto.CourseDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CourseServiceInterface {

    public CourseResponseDto saveCourse(CourseDto courseDto, String email) throws IOException;
    public void deleteByid(String cid);
    public List<CourseResponseDto> findByInstructor(String email);
    public Long getCourseSize(String type);
    public Courses findById(Long id);
    public void save(Courses course);
    public List<CourseResponseDto> findAllCourses(String page);
}
