package com.example.project.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Cart;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;
import com.example.project.exception.ConflictException;
import com.example.project.repository.CartRepository;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
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
		Users user = userRepo.findByEmail(email);
		int p = 0;
		try {
		 p = Integer.parseInt(page);
		}catch(NumberFormatException e)
		{
			throw new NumberFormatException("Enter valid Page Number");
		}
		Pageable pageable = PageRequest.of(p, 2);
		
		Page<Cart> pageSize = cartRepo.findByUser(user,pageable);
		
		return pageSize.map(cart -> new CourseResponseDto(cart.getCartCourse())).getContent();
	}


	@Transactional
	public int findcartSizeByUser(Users user) {
		// TODO Auto-generated method stub
		List<Cart> cart = cartRepo.findByUser(user);
		return cart.size();
	}



	@Transactional
	public CourseResponseDto save(String email,String courseid) {
		 
		Long c = 0l;
		
		try {
	       c = Long.parseLong(courseid);
		}catch(NumberFormatException e)
		{
			throw new NumberFormatException("No Course Found With THe Id");
		}
		
		Users user = userRepo.findByEmail(email);
		Courses course = courseRepo.findById(c).get();
		
		//check if already present
		if(cartRepo.findByUserAndCartCourse(user,course) != null)
		{
			throw new ConflictException("Allready Added in Cart");
		}
		
		Cart cart = new Cart();
		cart.addCourse(course);
		cart.addUser(user);
		
		Cart ca =  cartRepo.save(cart);
		
		return new  CourseResponseDto(ca.getCartCourse());
		
	}


	@Transactional
	public int findcartSizeByUser(String email) {
		Users user = userRepo.findByEmail(email);
		
		return cartRepo.countByUser(user);
	}
}
