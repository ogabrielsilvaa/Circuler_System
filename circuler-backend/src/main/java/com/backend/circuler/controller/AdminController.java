package com.backend.circuler.controller;

import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administração", description = "Endpoints exclusivos para administradores.")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{id}/promote")
    public ResponseEntity<UserResponseDTO> promoteToAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.promoteToAdmin(id));
    }
}