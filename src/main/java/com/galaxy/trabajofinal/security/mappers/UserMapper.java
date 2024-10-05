package com.galaxy.trabajofinal.security.mappers;

import com.galaxy.trabajofinal.security.DTOs.UserDTO;
import com.galaxy.trabajofinal.security.entities.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .user(user.getUserName())
                .password(user.getPassword())
                .state(user.getState())
                .build();
    }
}

