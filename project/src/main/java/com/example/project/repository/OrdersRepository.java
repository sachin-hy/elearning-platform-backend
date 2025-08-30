package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long>{

	Orders findByRazorpayOrderId(String razorpayOrderId);

}
