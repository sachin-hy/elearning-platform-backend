package com.example.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import com.example.project.dto.CategoryDto;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Category;
import com.example.project.entity.Courses;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.CourseRepository;

@Service
public class CategoryService {
   
	@Autowired
	private CategoryRepository categoryRepo;

	
	@Autowired
	private CourseRepository courseRepo;
	
	
	@Transactional
	public void save(CategoryDto categoryDto) {
		
		Category category = new Category();
		category.setName(categoryDto.name());
		category.setDescription(categoryDto.description());
		
		categoryRepo.save(category);
	}

	@Transactional
	public List<Category> getAllCategory() {
		
		return categoryRepo.findAll();
	}


	
	@Transactional
	public  Category findByid(Long cid) {
		// TODO Auto-generated method stub
		Optional<Category> category = categoryRepo.findById(cid);
		if(category.isPresent())
		{
			return category.get();
		}else
		{
			return null;
		}
	}
	@Transactional
	public List<Category> findNotById(Long cid) {
		
		return categoryRepo.findNotById(cid);
	}

	@Transactional
	public Category getByName(String type) {
		
		return categoryRepo.findByName(type);
	}

	
	@Transactional
	public   List<CourseResponseDto> getByCategory(String type, String page) {
		
		int p = 0;
		try {
			p = Integer.parseInt(page);
		}catch(NumberFormatException e)
		{
			throw new NumberFormatException("Enter Valid Page Number");
		}
		
		Pageable pageable = PageRequest.of(p, 2);
		
		if(!type.equals("All"))
		{
			Page<Courses> pa =  categoryRepo.getCourseByCategory(type,pageable);
		 
			return pa.map(course -> new CourseResponseDto(course)).getContent();
		
		}else {
			 Page<Courses> pa = courseRepo.findAll(pageable);
			
			 return pa.map(course -> new CourseResponseDto(course)).getContent();
		}
		
	}

	
}
