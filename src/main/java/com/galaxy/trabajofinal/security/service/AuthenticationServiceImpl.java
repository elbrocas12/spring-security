package com.galaxy.trabajofinal.security.service;

import com.galaxy.trabajofinal.security.DTOs.*;
import com.galaxy.trabajofinal.security.UTIL.TOTPUtil;
import com.galaxy.trabajofinal.security.entities.Role;
import com.galaxy.trabajofinal.security.entities.User;
import com.galaxy.trabajofinal.security.jwt.IJWTService;
import com.galaxy.trabajofinal.security.repository.RoleRepository;
import com.galaxy.trabajofinal.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements IAuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final IJWTService jWTService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TOTPUtil tOTPUtil;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationServiceException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.userName(), loginRequestDTO.password()));
        UserDetails userDetails= userDetailsService.loadUserByUsername(loginRequestDTO.userName());
        var user=this.userRepository.findByUserName(loginRequestDTO.userName());
        Long userId=user.get().getId();
        if (isNull(userDetails)) {
            throw new IllegalArgumentException("Invalid user or password.");
        }

        return jWTService.generateJwtTokenWithUserId(userDetails,userId);
    }

    @Override
    public SignupRespondeDTO signup(SignupRequestDTO signupRequestDTO) {

        Optional<User> existingUser=userRepository.findByUserName(signupRequestDTO.userName());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUserName(signupRequestDTO.userName());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequestDTO.password()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : signupRequestDTO.role()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);
        var savedUser=this.userRepository.save(user);
        return new SignupRespondeDTO("Creación exitosa de usuario",savedUser.getId());


    }

    @Override
    public LoginResponseDTO login(String code) throws AuthenticationServiceException {
        try {
            if (tOTPUtil.verifyCode(code)) {

                UserDTO userDTO = userService.findByCode(code);

                log.info("UserDTO- User {}", userDTO.getUser());

                UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUser());

                log.info("UserDetails {}", userDetails);

                if (isNull(userDetails)) {
                    throw new IllegalArgumentException("Invalid user or password.");
                }

                LoginResponseDTO loginResponseDTO = jWTService.generateJwtTokenWithUserId(userDetails, userDTO.getId());

                return loginResponseDTO;
            }else {
                throw new SecurityException("Código no válido");
            }
        } catch (Exception e) {
            System.out.println("attemptAuthentication " + e.getMessage());
            throw new SecurityException(e);
        }
    }

    @Override
    public LoginCodeDTO login2F(HttpServletRequest request, LoginRequestDTO loginRequestDTO) throws AuthenticationServiceException {
        try {

            log.info("loginRequest {}", loginRequestDTO);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.userName());

            log.info("userDetails {}", userDetails);

            if (!isNull(userDetails)) {

                if (bCryptPasswordEncoder.matches(loginRequestDTO.password(), userDetails.getPassword())) {

                    //
                    String code = tOTPUtil.generateCode();

                    var user=this.userRepository.findByUserName(userDetails.getUsername());
                    user.get().setCode2f(code);
                    this.userRepository.save(user.get());
                    // Save code
                    //userService.updateCodigo2F(code, loginRequestDTO.userName());

                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.userName(), loginRequestDTO.password(), userDetails.getAuthorities());

                    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(upat);

                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    return LoginCodeDTO.builder().code(code).build();
                }
            }

            throw new SecurityException("Login error");

        } catch (Exception e) {
            System.out.println("attemptAuthentication " + e.getMessage());
            throw new SecurityException(e);
        }
    }
}
