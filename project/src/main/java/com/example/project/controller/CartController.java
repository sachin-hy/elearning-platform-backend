package com.example.project.controller;

import java.security.Principal;
import java.util.List;


import com.example.project.service.Interface.CartServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.CourseResponseDto;

import com.example.project.service.CartService;

import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
public class CartController {

    @Autowired
	private CartServiceInterface cartService;
	

	@GetMapping("/cart")
	public ResponseEntity<?> cart(@RequestParam("page") String page, Principal principal)
	{
        
			String email = principal.getName();
			
			log.info("Get cart Detail Request Recived for user : {} ",email);
			
			List<CourseResponseDto> courselist = cartService.findcartCourseByUser(email, page);
			
			return new ResponseEntity<>(courselist,HttpStatus.OK);
		
	}
	
	
	
	
	@GetMapping("/cart-size")
	public ResponseEntity<?> cartSize(Principal principal)
	{
            log.info("Get cart size request recived for user : {}", principal.getName());
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
			
			log.info("Create Cart Request Recived for user : {}",email);
			
			// save the cart with user and course
			CourseResponseDto cart = cartService.save(email,courseid);
			
			
			return new ResponseEntity<>(cart,HttpStatus.OK);
		
	}
	
}
