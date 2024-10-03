package com.galaxy.trabajofinal.security.controller;

import com.galaxy.trabajofinal.security.DTOs.LoginRequestDTO;
import com.galaxy.trabajofinal.security.DTOs.SignupRequestDTO;
import com.galaxy.trabajofinal.security.service.IAuthenticationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthenticationService iAuthenticationService;

    @GetMapping()
    public ResponseEntity<?> testGet(){
        return ResponseEntity.ok("Get exitoso");
    }

    @PostMapping("createuser")
    public ResponseEntity<?> createUser(@RequestBody()SignupRequestDTO signupRequestDTO){
        var response=iAuthenticationService.signup(signupRequestDTO);
        return ResponseEntity.ok(response) ;
    }

    @PostMapping("login")
    public ResponseEntity<?> logIn(@RequestBody() LoginRequestDTO loginRequestDTO){
        var response=iAuthenticationService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }
}
