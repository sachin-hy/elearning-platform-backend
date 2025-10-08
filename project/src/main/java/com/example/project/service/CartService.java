package com.example.project.service;



import java.util.List;
import java.util.Optional;

import com.cloudinary.api.exceptions.NotFound;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.service.Interface.CartServiceInterface;
import com.example.project.service.Interface.CourseServiceInterface;
import com.example.project.service.Interface.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class CartService implements CartServiceInterface {

    @Autowired
	private  CartRepository cartRepo;
    @Autowired
    private UsersServiceInterface userService;
    @Autowired
    private CourseServiceInterface courseService;





	@Transactional
	public List<CourseResponseDto> findcartCourseByUser(String email ,String page)
	{
		log.info("findcartCourseByUser() called for user: {} with page: {}", email, page);

        Optional<Users> user = userService.findByEmail(email);

        if(user.isPresent()){

            int p = 0;
            try {
                p = Integer.parseInt(page);
            }catch(NumberFormatException e)
            {
                log.warn("Invalid page number provided: {}", page);
                throw new NumberFormatException("Enter valid Page Number");
            }
            Pageable pageable = PageRequest.of(p, 2);

            Page<Cart> pageSize = cartRepo.findByUser(user.get(),pageable);

            log.info("Found {} cart items for user: {}", pageSize.getTotalElements(), email);

            return pageSize.map(cart -> new CourseResponseDto(cart.getCartCourse())).getContent();

        }else{

            throw new UsernameNotFoundException("User not found");
        }

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
		
		Optional<Users> user = userService.findByEmail(email);
        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("User not found! Try Later");
        }

		Courses course = courseService.findById(c);

            if(cartRepo.findByUserAndCartCourse(user.get(),course) != null)
            {
                log.warn("Attempted to add a course already in cart. User: {}, Course ID: {}", email, courseid);
                throw new ConflictException("Allready Added in Cart");
            }

            Cart cart = new Cart();
            cart.addCourse(course);
            cart.addUser(user.get());

            Cart ca =  cartRepo.save(cart);

            log.info("Successfully added course ID: {} to cart for user: {}", courseid, email);

            return new  CourseResponseDto(ca.getCartCourse());

	}


	@Transactional
	public int findcartSizeByUser(String email) {
		log.info("findcartSizeByUser(String email) called for user: {}", email);
		Optional<Users> user = userService.findByEmail(email);

         if(user.isPresent())
         {
             int cartSize = cartRepo.countByUser(user.get());
             log.info("Returning count of {} for user's cart: {}", cartSize, email);
             return cartSize;
         }else{

             throw new UsernameNotFoundException("User not found! Try Later");
         }

	}


}
