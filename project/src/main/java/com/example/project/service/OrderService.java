package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import com.example.project.repository.ChatRoomRepository;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.OrdersRepository;
import com.example.project.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	
	
	@Autowired
	private OrdersRepository ordersRepo;
	
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private ChatRoomRepository chatRepo;
	
	
	@Transactional
	public Orders findOrder(PaymentVerificationRequest request)
	{
		Orders orderEntity = ordersRepo.findByRazorpayOrderId(request.razorpayOrderId());
		
//		orderEntity.setStatus(OrderStatus.SUCCESS);
//        orderEntity.setRazorpayPaymentId(request.razorpayPaymentId());
//        orderEntity.setRazorpaySignature(request.razorpaySignature());
//        orderEntity.setUpdatedAt(LocalDateTime.now());
//        ordersRepo.save(orderEntity);
        
        return orderEntity;
	}


	@Transactional
	public List<CourseResponseDto> saveOrderSuccess(Orders newOrder, String razorpayOrderId) {
		// TODO Auto-generated method stub
		newOrder.setStatus(OrderStatus.SUCCESS);
		newOrder.setUpdatedAt(LocalDateTime.now());
        newOrder.setRazorpayPaymentId(razorpayOrderId);
		Orders managedOrder = ordersRepo.save(newOrder);
		

        Users  user = managedOrder.getUser(); //userRepo.findByEmail(email);
		Courses course = managedOrder.getCourse();//courseRepo.findById(courseId).get();
	    
		ChatRoom chatRoom = course.getChatRoom(); //courseRepo.findChatRoomById(courseId);
	
		
		user.getChatRoom().add(chatRoom);
		chatRoom.getUsers().add(user);
		user.addCourse(course);

		Set<Courses> userCourses = user.getCourses();
		
	    return userCourses.stream().map(c -> new CourseResponseDto(c)).collect(Collectors.toList());
 
	}
	
	@Transactional
	public void saveOrder(Orders newOrder)
	{
		ordersRepo.save(newOrder);
	}

}
