package com.galaxy.trabajofinal.security.service;

import com.galaxy.trabajofinal.security.DTOs.LoginRequestDTO;
import com.galaxy.trabajofinal.security.DTOs.LoginResponseDTO;
import com.galaxy.trabajofinal.security.DTOs.SignupRequestDTO;
import com.galaxy.trabajofinal.security.DTOs.SignupRespondeDTO;
import org.springframework.security.authentication.AuthenticationServiceException;

public interface IAuthenticationService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationServiceException;
    SignupRespondeDTO signup(SignupRequestDTO signupRequestDTO);
}
