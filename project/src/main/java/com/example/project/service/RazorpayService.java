package com.example.project.service;

import java.time.LocalDateTime;


import com.example.project.service.Interface.RazorpayServiceInterface;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.project.dto.PaymentVerificationRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RazorpayService implements RazorpayServiceInterface {

	    @Value("${razorpay.key_id}")
	    private String keyId;
	    @Value("${razorpay.key_secret}")
	    private String keySecret;

        @Autowired
	    private  RazorpayClient razorpayClient;


	    
	@Transactional    
	public Order createRazorpayOrder(Long courseid,int coursePrice, String email) throws Exception {
	    // Order creation
		 log.info("Attempting to create Razorpay order for user: {} and course ID: {}", email, courseid);
	       
	    int amount = coursePrice;
	    String currency = "INR";

	    JSONObject orderRequest = new JSONObject();

	    orderRequest.put("amount", amount * 100);
	    orderRequest.put("currency", currency);
	    orderRequest.put("receipt", "order_rcpt_" + LocalDateTime.now()); 

	    
	    JSONObject notes = new JSONObject();
	    notes.put("courseId", courseid);
	    notes.put("email", email);

	    // putting notes in the orderRequest json object
	    orderRequest.put("notes", notes);
	  
	    return razorpayClient.orders.create(orderRequest);
	}
	
	public String getKeyId() {
        return keyId;
    }

	@Transactional
	public boolean verifyPaymentSignature(PaymentVerificationRequest request) {
		 log.info("Verifying payment signature for order ID: {}", request.razorpayOrderId());
	        
		try {
			JSONObject options = new JSONObject();
			options.put("razorpay_order_id", request.razorpayOrderId());
            options.put("razorpay_payment_id", request.razorpayPaymentId());
            options.put("razorpay_signature", request.razorpaySignature());
            
            return Utils.verifyPaymentSignature(options, keySecret);
		}catch(Exception e)
		{
			  log.error("An error occurred during payment signature verification for order ID: {}. Error: {}", request.razorpayOrderId(), e.getMessage(), e);
	          
			return false;
		}
	}

	
}
