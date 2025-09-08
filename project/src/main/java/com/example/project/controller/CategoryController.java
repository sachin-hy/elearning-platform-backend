package com.example.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.CategoryDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Category;
import com.example.project.service.CategoryService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

	
	private final CategoryService categoryService;
	
	
	public CategoryController(CategoryService categoryService)
	{
		this.categoryService=categoryService;
	}
	
	
	
	@PostMapping
	public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDto categoryDto)
	{
		
		
		if(categoryDto.name() == null || categoryDto.description() == null)
		{
			return new ResponseEntity<>("Require all field",HttpStatus.BAD_REQUEST);
		}
		
		
		
		try {
		
			categoryService.save(categoryDto);
			
			return new ResponseEntity<>("Tag created",HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@GetMapping
	public ResponseEntity<List<Category>> getAllCategorys()
	{
		try {
			List<Category> categoryList = categoryService.getAllCategory();
			
			return new ResponseEntity<>(categoryList,HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{type}/courses")
	public ResponseEntity<?> getCourseByCategory(@PathVariable String type,@RequestParam("page")  String page)   
	{
		
		List<CourseResponseDto> list = categoryService.getByCategory(type,page);
		
		
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	
}
