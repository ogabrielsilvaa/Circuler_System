package com.backend.circuler.controller;

import com.backend.circuler.dto.user.UserCreateDTO;
import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.dto.user.UserUpdateDTO;
import com.backend.circuler.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários na API.")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllActive() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findByIdActive(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByIdActive(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Integer id, @RequestBody UserUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
