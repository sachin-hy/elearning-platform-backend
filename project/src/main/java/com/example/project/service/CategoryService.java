package com.example.project.service;

import java.util.List;
import java.util.Optional;

import com.example.project.exception.ResourceNotFoundException;
import com.example.project.service.Interface.CategoryServiceInterface;
import com.example.project.service.Interface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService implements CategoryServiceInterface {

        @Autowired
	    private CategoryRepository categoryRepo;

	
	@Transactional
	public void save(CategoryDto categoryDto) {
		 log.info("Saving new category: {}", categoryDto.name());
	       
		Category category = new Category();
		category.setName(categoryDto.name());
		category.setDescription(categoryDto.description());
		
		categoryRepo.save(category);
		 log.info("Category saved successfully.");
	}
	
	
	

	@Transactional
    @Cacheable(value = "categoryCache", key = "'all_categories'")
	public List<Category> getAllCategory() {
		 log.info("Fetching all categories.");
	       
		  List<Category> categories = categoryRepo.findAll();
          log.info("Found {} categories.", categories.size());
          return categories;
	}

    @Override
    public Optional<Category> findById(Long cid) {
        return categoryRepo.findById(cid);
    }

    @Override
    public Optional<Category> findByName(String type) {
        return categoryRepo.findByName(type);
    }


    @Transactional
    @Cacheable(value = "coursesByCategory", key = "#type + '-' + #page")
	public   List<CourseResponseDto> getByCategory(String type, String page) {

        log.info("Fetching courses for category '{}' on page '{}'", type, page);
	      
		int p = 0;
		try {
			p = Integer.parseInt(page);
		}catch(NumberFormatException e)
		{
            log.warn("Invalid page number '{}' entered for category: {}", page, type);
        
			throw new NumberFormatException("Enter Valid Page Number");
		}
		
		Pageable pageable = PageRequest.of(p, 2);
		

			Page<Courses> pa =  categoryRepo.getCourseByCategory(type,pageable);
			log.info("Found {} courses for category '{}'.", pa.getTotalElements(), type);
	        
			return pa.map(course -> new CourseResponseDto(course)).getContent();
		

		
	}


}
