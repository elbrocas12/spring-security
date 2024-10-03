package com.galaxy.trabajofinal.security.jwt;

import com.galaxy.trabajofinal.security.DTOs.LoginResponseDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJWTService {
    LoginResponseDTO generateJwtToken(UserDetails userDetails);
    LoginResponseDTO generateJwtTokenWithUserId(UserDetails userDetails,Long userId);
    //String generateJwtTokenFromRefreshToken(UserDetails userDetails);

    String  getUserNameFromJwtToken(String token);

    Claims getAllClaims(String token);

    boolean validateJwtToken(String token); // owner, expiration

    String getJwtToken(HttpServletRequest request);

}
