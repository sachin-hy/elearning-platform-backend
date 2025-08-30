package com.example.project.entity;

import java.time.LocalDateTime;

import com.example.project.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
	
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
  
   
   @Column(nullable = false)
   private Double amount;
   
   @Column(name = "currency", nullable = false, length = 10)
   private String currency = "INR";
   

   @Column(name = "razorpay_order_id", unique = true)
   private String razorpayOrderId;

   @Column(name = "razorpay_payment_id")
   private String razorpayPaymentId; 
   
   @Column(name = "razorpay_signature", columnDefinition = "TEXT")
   private String razorpaySignature;
  
   @Enumerated(EnumType.STRING)
   @Column(nullable = false, length = 20)
   private OrderStatus status;
   
   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();
   
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;
   
   @Column(name = "failure_reason", columnDefinition = "TEXT")
   private String failureReason;
   
   
   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private Users user;
   
   @ManyToOne
   @JoinColumn(name = "course_id", nullable = false)
   private Courses course;
  
}
