package com.backend.circuler.controller;

import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administração", description = "Endpoints exclusivos para administrador raiz.")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{id}/promote")
    @Operation(
            summary = "Administrador do Sistema - Promover usuário para administrador de ponto",
            description = "Concede a um usuário comum a role de administrador de Ponto de Coleta, habilitando-o a ser responsável por um ponto. Endpoint disponível somente para o Administrador do Sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário promovido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador do Sistema"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo"),
            @ApiResponse(responseCode = "422", description = "Usuário já possui a role de administrador")
    })
    public ResponseEntity<UserResponseDTO> promoteToAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.promoteToAdmin(id));
    }
}
