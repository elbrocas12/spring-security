package com.galaxy.trabajofinal.security.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record LoginRequestDTO(
		@NotEmpty(message = "Username is requerid")
		String userName, 
		@NotEmpty(message = "Password is requerid")
		String password
		
		) {

}
