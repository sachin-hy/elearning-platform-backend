package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.dto.CourseResponseDto;
import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.entity.ChatRoom;
import com.example.project.entity.Courses;
import com.example.project.entity.Orders;
import com.example.project.entity.Users;
import com.example.project.enums.OrderStatus;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	
	
	 private final OrdersRepository ordersRepo;

	    public OrderService(OrdersRepository ordersRepo) {
	        this.ordersRepo = ordersRepo;
	    }
	
	
	
	@Transactional
	public Orders findOrder(PaymentVerificationRequest request)
	{
		log.info("Finding order with Razorpay order ID: {}", request.razorpayOrderId());

		Orders orderEntity = ordersRepo.findByRazorpayOrderId(request.razorpayOrderId());
		
		 log.info("Order found for ID: {}", request.razorpayOrderId());
	       
        return orderEntity;
	}


	@Transactional
	public List<CourseResponseDto> saveOrderSuccess(Orders newOrder, String razorpayOrderId) {
		// TODO Auto-generated method stub
		
		 log.info("Updating order ID {} to SUCCESS status with payment ID: {}", newOrder.getId());

		
		newOrder.setStatus(OrderStatus.SUCCESS);
		newOrder.setUpdatedAt(LocalDateTime.now());
        newOrder.setRazorpayPaymentId(razorpayOrderId);
		Orders managedOrder = ordersRepo.save(newOrder);
		

        Users  user = managedOrder.getUser(); //userRepo.findByEmail(email);
		Courses course = managedOrder.getCourse();//courseRepo.findById(courseId).get();
	    
		ChatRoom chatRoom = course.getChatRoom(); //courseRepo.findChatRoomById(courseId);
	    
		 if (chatRoom == null) {
	            log.warn("Chat room not found for course ID: {}", course.getCourseid());
	            throw new ResourceNotFoundException("Chat room not found for the course.");
	        }
		
		user.getChatRoom().add(chatRoom);
		chatRoom.getUsers().add(user);
		user.addCourse(course);

		log.info("User {} successfully enrolled in course ID: {}", user.getEmail(), course.getCourseid());

		Set<Courses> userCourses = user.getCourses();
		
		 log.info("Returning {} courses for user: {}", userCourses.size(), user.getEmail());

	    return userCourses.stream().map(c -> new CourseResponseDto(c)).collect(Collectors.toList());
 
	}
	
	@Transactional
	public void saveOrder(Orders newOrder)
	{
		log.info("Saving new order with ID: {}", newOrder.getId());
	       
		ordersRepo.save(newOrder);
		  log.info("New order saved successfully.");
	}

}
