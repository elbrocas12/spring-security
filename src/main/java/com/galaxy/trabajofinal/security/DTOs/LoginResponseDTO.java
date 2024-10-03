package com.galaxy.trabajofinal.security.DTOs;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String token,Long userId) {

}
