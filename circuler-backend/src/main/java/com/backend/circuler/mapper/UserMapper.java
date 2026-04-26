package com.backend.circuler.mapper;

import com.backend.circuler.dto.user.UserCreateDTO;
import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserCreateDTO createDTO) {
        User user = new User();
        user.setName(createDTO.getName());
        user.setEmail(createDTO.getEmail());
        user.setPassword(createDTO.getPassword());
        user.setStatus(UserStatus.ATIVO);
        return user;
    }

    public UserResponseDTO toDto(User entity) {
        Set<String> roleNames = entity.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getStatus(),
                roleNames
        );
    }
}