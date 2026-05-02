package com.backend.circuler.controller;

import com.backend.circuler.dto.user.UserCreateDTO;
import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.dto.user.UserUpdateDTO;
import com.backend.circuler.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(
            summary = "Cadastrar Usuário",
            description = "Cria um novo usuário no sistema. Endpoint público — não requer autenticação. O usuário é criado com a role ROLE_USER por padrão."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou inválidos no corpo da requisição"),
            @ApiResponse(responseCode = "422", description = "O e-mail informado já está em uso por outro usuário")
    })
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    @Operation(
            summary = "Listar Usuários Ativos",
            description = "Retorna todos os usuários com status diferente de APAGADO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    public ResponseEntity<List<UserResponseDTO>> findAllActive() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Usuário por ID",
            description = "Retorna os dados de um usuário ativo específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo")
    })
    public ResponseEntity<UserResponseDTO> findByIdActive(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByIdActive(id));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Listar Todos os Usuários",
            description = "Retorna todos os usuários cadastrados no sistema, incluindo os marcados como APAGADO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Atualizar Usuário",
            description = "Atualiza parcialmente os dados de um usuário. Apenas os campos enviados no corpo da requisição serão alterados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo"),
            @ApiResponse(responseCode = "422", description = "O e-mail informado já está em uso por outro usuário")
    })
    public ResponseEntity<UserResponseDTO> update(@PathVariable Integer id, @RequestBody UserUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "ADMIN RAIZ - Remover Usuário",
            description = "Realiza a exclusão lógica de um usuário, marcando-o como APAGADO. Endpoint disponível somente para o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador Raiz"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
