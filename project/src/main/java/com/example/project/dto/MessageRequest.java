package com.example.project.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

	@NotEmpty(message ="Contetn can Not be Null")
	private String content;
	@NotBlank(message = "RoomID Can not be Null")
	private String roomId;
	@NotBlank(message = "Email Can Not be null")
	private String email;
	
	
}
