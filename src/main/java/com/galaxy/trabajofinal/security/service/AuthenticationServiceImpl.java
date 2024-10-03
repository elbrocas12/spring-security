package com.galaxy.trabajofinal.security.service;

import com.galaxy.trabajofinal.security.DTOs.LoginRequestDTO;
import com.galaxy.trabajofinal.security.DTOs.LoginResponseDTO;
import com.galaxy.trabajofinal.security.DTOs.SignupRequestDTO;
import com.galaxy.trabajofinal.security.DTOs.SignupRespondeDTO;
import com.galaxy.trabajofinal.security.entities.Role;
import com.galaxy.trabajofinal.security.entities.User;
import com.galaxy.trabajofinal.security.jwt.IJWTService;
import com.galaxy.trabajofinal.security.repository.RoleRepository;
import com.galaxy.trabajofinal.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
}
