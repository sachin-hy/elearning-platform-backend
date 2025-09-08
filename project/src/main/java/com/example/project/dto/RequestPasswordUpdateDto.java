package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestPasswordUpdateDto(
	@NotBlank(message = "OldPassword Field Can Not be Null")	
    String oldPassword,
    @NotBlank(message = "NEw Password Field Can not Be null")
    String newPassword,
    @NotBlank(message = "Confirm Password Filed Can not be Null")
    String confirmedNewPassword
) {}
