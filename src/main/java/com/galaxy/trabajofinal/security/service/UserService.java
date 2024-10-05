package com.galaxy.trabajofinal.security.service;

import com.galaxy.trabajofinal.security.DTOs.UserDTO;

public interface UserService {

    Boolean updateCodigo2F(String code, String userName) throws UserServiceException;

    UserDTO findByCode(String code)throws UserServiceException;
}
