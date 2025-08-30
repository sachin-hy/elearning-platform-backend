package com.example.project.controller;

import com.example.project.dto.OrderResponse;
import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.dto.PaymentVerificationResponse;
import com.example.project.entity.Courses;
import com.example.project.entity.Orders;
import com.example.project.entity.Users;
import com.example.project.enums.OrderStatus;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ResourceNotFoundException;

import com.example.project.service.CourseService;
import com.example.project.service.EmailService;
import com.example.project.service.OrderService;
import com.example.project.service.RazorpayService;
import com.example.project.service.UsersService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@Slf4j 
public class CapturePaymentController {

   
    @Autowired
    private CourseService courseService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RazorpayService razorpayService;
    @Autowired
    private OrderService orderService;

  
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam("courseid") String cid, Principal principal) {
        try {
           
        	System.out.println("====================/create order controler is called");
        	log.info("====================/create order controler is called");
            if (cid == null || cid.isBlank()) {
                log.warn("Course ID is missing in the request.");
                
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course ID is required.");
            }

            long courseId;
            try {
                courseId = Long.parseLong(cid);
            } catch (NumberFormatException e) {
                log.warn("Invalid Course ID format: {}", cid);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Course ID format.");
            }

            
            String email = principal.getName();
            Users user = usersService.findByEmail(email);
            if (user == null) {
                log.error("User not found for email: {}", email);
                throw new ResourceNotFoundException("User not found with email: " + email);
            }

            Courses course = courseService.findById(courseId);
            if (course == null) {
                log.warn("Attempt to purchase a non-existent course with ID: {}", courseId);
                throw new ResourceNotFoundException("Course not found with ID: " + courseId);
            }

           
            if (courseService.getUserEnrolled(user, courseId)) {
                log.info("User {} already purchased course {}", email, courseId);
                throw new ConflictException("You have already purchased this course.");
            }

            // --- Razorpay Order Creation ---
            log.info("Creating Razorpay order for user {} and course {}", email, courseId);
            Order razorpayOrder = razorpayService.createRazorpayOrder(course.getCourseid(), course.getPrice(), email);
            
            int amountPaid = razorpayOrder.get("amount");
            double amount = amountPaid / 100.0; 

            // -Save Order to Database ---
            Orders newOrder = new Orders();
            newOrder.setUser(user);
            newOrder.setCourse(course);
            newOrder.setAmount(amount);
            newOrder.setCurrency(razorpayOrder.get("currency"));
            newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
            newOrder.setStatus(OrderStatus.PENDING);
            orderService.saveOrder(newOrder);
            log.info("Order {} saved to the database for user {}", newOrder.getRazorpayOrderId(), email);

            // --- Prepare Response ---
            OrderResponse response = new OrderResponse(
                    razorpayOrder.get("id"),
                    razorpayOrder.get("currency"),
                    amount,
                    razorpayService.getKeyId());

            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {
            log.error("Razorpay API error while creating order: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error creating payment order. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (ResourceNotFoundException  | ConflictException e) {
           
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResponseStatusException e) {
          
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        } catch (Exception e) {
           
            log.error("An unexpected error occurred in /create-order: {}", e.getMessage(), e);
            return new ResponseEntity<>("An internal server error occurred. Please contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
    @PostMapping("/verify-payment")
    public ResponseEntity<PaymentVerificationResponse> verifyPayment(@RequestBody PaymentVerificationRequest request, Principal principal) {
        try {
        	
        	
            log.info("Verifying payment for order ID: {}", request.razorpayOrderId());
            
            // --- Signature Verification ---
            boolean isVerified = razorpayService.verifyPaymentSignature(request);

            // --- Fetch Order from DB ---
            Orders orderEntity = orderService.findOrder(request);
            if (orderEntity == null) {
                log.error("Order not found in DB for Razorpay Order ID: {}", request.razorpayOrderId());
                // This is a critical issue, as an order should exist.
                return new ResponseEntity<>(new PaymentVerificationResponse(false, "Order details not found."), HttpStatus.NOT_FOUND);
            }
              
            if (orderEntity.getStatus() == OrderStatus.SUCCESS) {
                log.info("Order {} has already been processed.", request.razorpayOrderId());
                return ResponseEntity.ok(new PaymentVerificationResponse(true, "Payment already verified!"));
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
                    // Log the email error but don't fail the entire transaction
                    log.error("Failed to send confirmation email to {}: {}", email, e.getMessage());
                }

                return ResponseEntity.ok(new PaymentVerificationResponse(true, "Payment verified successfully!"));
            } else {
                log.warn("Payment verification failed (signature mismatch) for order ID: {}", request.razorpayOrderId());
                
                // --- Update Order for Failed Payment ---
                orderEntity.setStatus(OrderStatus.FAILED);
                orderEntity.setFailureReason("Signature mismatch");
                orderEntity.setUpdatedAt(LocalDateTime.now());
                orderService.saveOrder(orderEntity);

                return ResponseEntity.ok(new PaymentVerificationResponse(false, "Payment verification failed!"));
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred in /verify-payment: {}", e.getMessage(), e);
            return new ResponseEntity<>(new PaymentVerificationResponse(false, "An internal server error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}