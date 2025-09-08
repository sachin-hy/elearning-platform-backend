package com.example.project.controller;

import com.example.project.dto.OrderResponse;
import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.dto.PaymentVerificationResponse;

import com.example.project.service.PaymentService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@Slf4j 
public class CapturePaymentController {

   
   
  
    private final PaymentService paymentService;
    
    
    public CapturePaymentController(PaymentService paymentService)
    {
    	this.paymentService = paymentService;
    }
    
    
  
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam("courseid") String cid, Principal principal) {
       
            String email = principal.getName();
            
            log.info("Create Order Request recived by user:  {}",email);
            OrderResponse response =  paymentService.createOrder(cid, email);
            return ResponseEntity.ok(response);

    }

   
    
    @PostMapping("/verify-payment")
    public ResponseEntity<PaymentVerificationResponse> verifyPayment(@Valid @RequestBody PaymentVerificationRequest request, Principal principal) {
      
    	log.info("Payment verification request recived by user : {} ",principal.getName());
    	PaymentVerificationResponse payment = paymentService.verifyPayment(request);
    	
    	return new ResponseEntity<>(payment,HttpStatus.OK);
    	
    }
}