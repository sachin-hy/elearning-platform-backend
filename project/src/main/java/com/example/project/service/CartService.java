package com.example.project.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.project.controller.CartController;
import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Cart;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;
import com.example.project.exception.ConflictException;
import com.example.project.repository.CartRepository;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {

	
	private final CartRepository cartRepo;
    private final UsersRepository userRepo;
    private final CourseRepository courseRepo;

    public CartService(CartRepository cartRepo, UsersRepository userRepo, CourseRepository courseRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }
	
	
	@Transactional
	public List<CourseResponseDto> findcartCourseByUser(String email ,String page)
	{
		log.info("findcartCourseByUser() called for user: {} with page: {}", email, page);
		Users user = userRepo.findByEmail(email);
		int p = 0;
		try {
		 p = Integer.parseInt(page);
		}catch(NumberFormatException e)
		{
			log.warn("Invalid page number provided: {}", page);
			throw new NumberFormatException("Enter valid Page Number");
		}
		Pageable pageable = PageRequest.of(p, 2);
		
		Page<Cart> pageSize = cartRepo.findByUser(user,pageable);
		
		log.info("Found {} cart items for user: {}", pageSize.getTotalElements(), email);
		
		return pageSize.map(cart -> new CourseResponseDto(cart.getCartCourse())).getContent();
	}


	@Transactional
	public int findcartSizeByUser(Users user) {
		// TODO Auto-generated method stub
		log.info("findcartSizeByUser() called for user: {}", user.getEmail());
		List<Cart> cart = cartRepo.findByUser(user);
		log.info("Returning cart size of {} for user: {}", cart.size(), user.getEmail());
		return cart.size();
	}



	@Transactional
	public CourseResponseDto save(String email,String courseid) {
		 
		log.info("save() called to add courseId: {} to user: {}", courseid, email);
		Long c = 0l;
		
		try {
	       c = Long.parseLong(courseid);
		}catch(NumberFormatException e)
		{
			log.warn("Invalid course number provided: {}", c);
			throw new NumberFormatException("No Course Found With THe Id");
		}
		
		Users user = userRepo.findByEmail(email);
		Courses course = courseRepo.findById(c).get();
		
		//check if already present
		if(cartRepo.findByUserAndCartCourse(user,course) != null)
		{
			log.warn("Attempted to add a course already in cart. User: {}, Course ID: {}", email, courseid);
			throw new ConflictException("Allready Added in Cart");
		}
		
		Cart cart = new Cart();
		cart.addCourse(course);
		cart.addUser(user);
		
		Cart ca =  cartRepo.save(cart);
		
		log.info("Successfully added course ID: {} to cart for user: {}", courseid, email);
		
		return new  CourseResponseDto(ca.getCartCourse());
		
	}


	@Transactional
	public int findcartSizeByUser(String email) {
		log.info("findcartSizeByUser(String email) called for user: {}", email);
		Users user = userRepo.findByEmail(email);
		int cartSize = cartRepo.countByUser(user);
		log.info("Returning count of {} for user's cart: {}", cartSize, email);
		return cartSize;
	}
}
