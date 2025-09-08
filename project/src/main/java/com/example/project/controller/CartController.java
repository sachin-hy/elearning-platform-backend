package com.example.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.CourseResponseDto;
import com.example.project.entity.Cart;
import com.example.project.entity.Courses;
import com.example.project.entity.Users;
import com.example.project.service.CartService;
import com.example.project.service.CourseService;
import com.example.project.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@RestController
public class CartController {
 
	private final CartService cartService;
	
	public CartController(CartService cartService)
	{
		this.cartService =cartService;
	}
	
	@GetMapping("/cart")
	public ResponseEntity<?> cart(@RequestParam("page") String page, Principal principal)
	{

			String email = principal.getName();
			
			List<CourseResponseDto> courselist = cartService.findcartCourseByUser(email, page);
			
			return new ResponseEntity<>(courselist,HttpStatus.OK);
		
	}
	
	
	
	
	@GetMapping("/cart-size")
	public ResponseEntity<?> cartSize(Principal principal)
	{
        
			String email = principal.getName();
			//Users user = usersService.findByEmail(email);
			int size = cartService.findcartSizeByUser(email);
			return new ResponseEntity<>(size,HttpStatus.OK);
		
	}
	
	@PostMapping("/cart")
	public ResponseEntity<?> createCart(@RequestParam("courseid") String courseid,
			                             Principal principal)
	{
			String email = principal.getName();
			
			
			// save the cart with user and course
			CourseResponseDto cart = cartService.save(email,courseid);
			
			
			return new ResponseEntity<>(cart,HttpStatus.OK);
		
	}
	
}
