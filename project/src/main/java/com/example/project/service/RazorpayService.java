package com.example.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.entity.Courses;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class RazorpayService {

	@Value("${razorpay.key_id}")
	private String keyId;
	@Value("${razorpay.key_secret}")
	private String keySecret;
	
	@Autowired
	private RazorpayClient razorpayClient;
	
	@Autowired
	private CourseService courseService;
	
	public Order createRazorpayOrder(Long courseid,int coursePrice, String email) throws Exception {
	    // Order creation
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

	public boolean verifyPaymentSignature(PaymentVerificationRequest request) {
		try {
			JSONObject options = new JSONObject();
			options.put("razorpay_order_id", request.razorpayOrderId());
            options.put("razorpay_payment_id", request.razorpayPaymentId());
            options.put("razorpay_signature", request.razorpaySignature());
            
            return Utils.verifyPaymentSignature(options, keySecret);
		}catch(Exception e)
		{
			return false;
		}
	}

	
}
