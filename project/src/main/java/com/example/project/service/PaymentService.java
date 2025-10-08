package com.example.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.project.service.Interface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.project.dto.OrderResponse;
import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.dto.PaymentVerificationResponse;
import com.example.project.entity.Courses;
import com.example.project.entity.Orders;
import com.example.project.entity.Users;
import com.example.project.enums.OrderStatus;
import com.example.project.exception.BadRequestException;
import com.example.project.exception.ConflictException;
import com.example.project.exception.InternalServerError;
import com.example.project.exception.ResourceNotFoundException;
import com.razorpay.Order;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
public class PaymentService implements PaymentServiceInterface{

    @Autowired
	private UsersServiceInterface userService;
    @Autowired
    private RazorpayServiceInterface razorpayService;
    @Autowired
    private CourseServiceInterface courseService;
    @Autowired
    private OrderServiceInterface orderService;
    @Autowired
    private EmailServiceInterface emailService;

    
    
    
    
	@Transactional
	public OrderResponse createOrder(String cid,String email) 
	{
		
		 if (cid == null || cid.isBlank()) {
             log.warn("Course ID is missing in the request.");
             
             throw new BadRequestException("Course ID is required.");
         }
		 
		 
		 long courseId;
         try {
             courseId = Long.parseLong(cid);
         } catch (NumberFormatException e) {
             log.warn("Invalid Course ID format: {}", cid);
             throw new NumberFormatException();
         }
         
         Optional<Users> u = userService.findByEmail(email);
		 
         if (u.isEmpty()) {
             log.error("User not found for email: {}", email);
             throw new ResourceNotFoundException("User not found with email: " + email);
         }

         Users user = u.get();
         
         Courses course = courseService.findById(courseId);
        		    //.orElseThrow(() -> new ResourceNotFoundException("Course Not Found! Try Again"));
         
         if (userService.existsByUseridAndCourses_Courseid(user.getUserid(), courseId)) {
        	   log.info("User {} already purchased course {}", email, courseId);
        	    throw new ConflictException("You have already purchased this course.");
        	}
         
         
         // create razorpay corder using razorpayservice class
         
         Order razorpayOrder=null;
         try {
          razorpayOrder = razorpayService.createRazorpayOrder(course.getCourseid(), course.getPrice(), email);
         }catch(Exception e)
         {
        	 log.error(""+e);
        	 throw new InternalServerError("Server error! Try Again");
         }
         
         
         int amountPaid = razorpayOrder.get("amount");
         double amount = amountPaid / 100.0; 
         
         //create new order to save in database
         Orders newOrder = new Orders();
         newOrder.setUser(user);
         newOrder.setCourse(course);
         newOrder.setAmount(amount);
         newOrder.setCurrency(razorpayOrder.get("currency"));
         newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
         newOrder.setStatus(OrderStatus.PENDING);
         
         orderService.saveOrder(newOrder);
         
        return new OrderResponse(
                 razorpayOrder.get("id"),
                 razorpayOrder.get("currency"),
                 amount,
                 razorpayService.getKeyId());
         
	}
	
	
	
	
	@Transactional
	public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest request)
	{
		  boolean isVerified = razorpayService.verifyPaymentSignature(request);
	      
		  Orders orderEntity = orderService.findOrder(request);
	
		  
		  if (orderEntity == null) {
              log.error("Order not found in DB for Razorpay Order ID: {}", request.razorpayOrderId());
              // This is a critical issue, as an order should exist.
              return new PaymentVerificationResponse(false, "Order details not found.");
          }
            
          if (orderEntity.getStatus() == OrderStatus.SUCCESS) {
              log.info("Order {} has already been processed.", request.razorpayOrderId());
              return new PaymentVerificationResponse(true, "Payment already verified!");
          }
          
          
          
          if (isVerified) {
              log.info("Payment signature verified for order ID: {}", request.razorpayOrderId());
              
              // --- Update Order Status and Enroll User ---
           
              orderService.saveOrderSuccess(orderEntity,request.razorpayOrderId());
              
              //long courseId = orderEntity.getCourse().getCourseid();
              String email = orderEntity.getUser().getEmail();

              
              log.info("User {} successfully enrolled in course {}", email);

              // --- Send Confirmation Email ---
              try {
                  String subject = "Congratulations from Your Website!";
                  String text = "Congratulations! You are now enrolled in the " + orderEntity.getCourse().getCourseName() + " course.";
                  emailService.sendEmail(email, subject, text);
                  log.info("Confirmation email sent to {}", email);
              } catch (Exception e) {
                  // Log the email error but don't fail 
                  log.error("Failed to send confirmation email to {}: {}", email, e.getMessage());
              }

              return new PaymentVerificationResponse(true, "Payment verified successfully!");
          } else {
              log.warn("Payment verification failed (signature mismatch) for order ID: {}", request.razorpayOrderId());
              
              // --- Update Order for Failed Payment ---
              orderEntity.setStatus(OrderStatus.FAILED);
              orderEntity.setFailureReason("Signature mismatch");
              orderEntity.setUpdatedAt(LocalDateTime.now());
              orderService.saveOrder(orderEntity);

              return new PaymentVerificationResponse(false, "Payment verification failed!");
          }
	}
}
