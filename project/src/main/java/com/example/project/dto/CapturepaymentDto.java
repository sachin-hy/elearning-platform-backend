package com.example.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CapturepaymentDto(
		@Min(value = 1,message = "Price Value can not be less then 1")
		int amount,
		@NotBlank(message = "Course Name Can not be Null")
        String courseName,
	    @NotBlank(message = "CourseDescription Can not Be null")
	    String courseDescription,
	    @NotBlank(message = "Orderid can not be null")
	    String orderid,
	    @NotBlank(message = "Thumbanil can not be null")
	    String thumbnail,
	    String currency
	   
	) {}
