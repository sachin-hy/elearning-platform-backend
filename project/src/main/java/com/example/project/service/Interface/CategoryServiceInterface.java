package com.example.project.service.Interface;

import com.example.project.dto.CategoryDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryServiceInterface {

    public void save(CategoryDto categoryDto);
    public   List<CourseResponseDto> getByCategory(String type, String page);
    public List<Category> getAllCategory();

    public  Optional<Category> findById(Long cid);

    Optional<Category> findByName(String type);
}
