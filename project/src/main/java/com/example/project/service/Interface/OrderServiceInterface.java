package com.example.project.service.Interface;

import com.example.project.dto.CourseResponseDto;
import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.entity.Orders;

import java.util.List;

public interface OrderServiceInterface {

    public Orders findOrder(PaymentVerificationRequest request);

    public List<CourseResponseDto> saveOrderSuccess(Orders newOrder, String razorpayOrderId);
    public void saveOrder(Orders newOrder);
}
