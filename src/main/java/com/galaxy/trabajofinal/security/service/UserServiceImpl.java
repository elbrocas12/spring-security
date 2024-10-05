package com.galaxy.trabajofinal.security.service;

import com.galaxy.trabajofinal.security.DTOs.UserDTO;
import com.galaxy.trabajofinal.security.entities.User;
import com.galaxy.trabajofinal.security.mappers.UserMapper;
import com.galaxy.trabajofinal.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Override
    public Boolean updateCodigo2F(String code, String userName) throws UserServiceException {
        try {
            userRepository.updateCode2f(code, userName);
            return true;
        } catch (Exception e) {
            throw new UserServiceException(e);
        }

    }

    @Override
    public UserDTO findByCode(String code) throws UserServiceException {
        try {

            var userEntity= userRepository.findByCode(code).orElseThrow(()->new UserServiceException("Code not found"));
            return UserMapper.toUserDTO(userEntity);
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }
}
