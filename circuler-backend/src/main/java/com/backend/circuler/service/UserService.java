package com.backend.circuler.service;

import com.backend.circuler.dto.user.UserCreateDTO;
import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.dto.user.UserUpdateDTO;
import com.backend.circuler.entity.Role;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.mapper.UserMapper;
import com.backend.circuler.repository.RoleRepository;
import com.backend.circuler.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso.");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER não encontrada."));

        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(userRole);

        User savedUser = repository.save(user);
        return mapper.toDto(savedUser);
    }

    public List<UserResponseDTO> findAllActive() {
        return repository.findAllByStatusNot(UserStatus.APAGADO)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findByIdActive(Integer id) {
        User user = repository.findByIdAndStatusNot(id, UserStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou apagado."));
        return mapper.toDto(user);
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO update(Integer id, UserUpdateDTO request) {
        User existingUser = repository.findByIdAndStatusNot(id, UserStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou apagado."));

        if (request.getName() != null && !request.getName().isBlank()) {
            existingUser.setName(request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!existingUser.getEmail().equals(request.getEmail())) {
                if (repository.findByEmail(request.getEmail()).isPresent()) {
                    throw new RuntimeException("Este e-mail já está em uso por outro usuário.");
                }
                existingUser.setEmail(request.getEmail());
            }
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getStatus() != null) {
            existingUser.setStatus(request.getStatus());
        }

        User updatedUser = repository.save(existingUser);
        return mapper.toDto(updatedUser);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        repository.logicalDeleteById(id, UserStatus.APAGADO);
    }

    @Transactional
    public UserResponseDTO promoteToAdmin(Integer id) {
        User user = repository.findByIdAndStatusNot(id, UserStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou apagado."));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (isAdmin) {
            throw new RuntimeException("Usuário já possui a role de administrador.");
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ROLE_ADMIN não encontrada."));

        user.getRoles().add(adminRole);
        User updatedUser = repository.save(user);
        return mapper.toDto(updatedUser);
    }
}