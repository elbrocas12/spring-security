package com.galaxy.trabajofinal.security.controller;

import com.galaxy.trabajofinal.security.DTOs.LoginCodeDTO;
import com.galaxy.trabajofinal.security.DTOs.LoginRequestDTO;
import com.galaxy.trabajofinal.security.DTOs.LoginResponseDTO;
import com.galaxy.trabajofinal.security.DTOs.SignupRequestDTO;
import com.galaxy.trabajofinal.security.service.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("log2f")
    public ResponseEntity<?> login2F(HttpServletRequest request, @RequestBody LoginRequestDTO loginRequestDTO){
        try {

            LoginCodeDTO loginCodeDTO = iAuthenticationService.login2F(request, loginRequestDTO);
            log.info("Code {}", loginCodeDTO.code());

            return ResponseEntity.ok().body(loginCodeDTO);

        } catch (AuthenticationServiceException e) {
            e.printStackTrace();
            Map<String, String> body = new HashMap<>();
            body.put("error", "Error interno");
            return ResponseEntity.internalServerError().body(body);
        }
    }
    @PostMapping("/verify-code/{code}")
    public ResponseEntity<?> verifyCode(@PathVariable String code) {
        try {

            log.info("code {}", code);

            LoginResponseDTO loginResponseDTO = iAuthenticationService.login(code);

            return ResponseEntity.ok().body(loginResponseDTO);

        } catch (AuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("attemptAuthentication " + e.getMessage());
            Map<String, String> body = new HashMap<>();
            body.put("error", "Error interno");
            return ResponseEntity.internalServerError().body(body);
        }
    }

}
