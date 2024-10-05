package com.galaxy.trabajofinal.security.service;

import com.galaxy.trabajofinal.security.DTOs.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;

public interface IAuthenticationService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationServiceException;
    SignupRespondeDTO signup(SignupRequestDTO signupRequestDTO);

    LoginResponseDTO login(String code) throws AuthenticationServiceException;

    LoginCodeDTO login2F(HttpServletRequest request, LoginRequestDTO loginRequestDTO) throws AuthenticationServiceException;
}
