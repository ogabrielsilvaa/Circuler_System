package com.backend.circuler.controller;

import com.backend.circuler.dto.collectionpoint.CollectionPointCreateDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointDetailDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointResponseDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointUpdateDTO;
import com.backend.circuler.service.CollectionPointService;
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
@RequestMapping("/api/collection-points")
@Tag(name = "Pontos de Coleta", description = "Endpoints para gerenciamento dos pontos de coleta de livros.")
public class CollectionPointController {

    private final CollectionPointService service;

    public CollectionPointController(CollectionPointService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "ADMIN RAIZ - Cadastrar um Ponto de Coleta",
            description = "Cadastra um novo Ponto de Coleta no sistema. Endpoint disponível somente para o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Ponto de coleta cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionPointResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou inválidos no corpo da requisição"),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador Raiz"),
            @ApiResponse(responseCode = "404", description = "O 'userAdminId' informado não existe ou está inativo"),
            @ApiResponse(responseCode = "422", description = "O 'userAdminId' não possui ROLE_ADMIN | O 'userAdminId' já é responsável por outro ponto de coleta")
    })
    public ResponseEntity<CollectionPointResponseDTO> create(@Valid @RequestBody CollectionPointCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os Pontos de Coleta",
            description = "Retorna todos os Pontos de Coleta ativos cadastrados no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionPointResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
    })
    public ResponseEntity<List<CollectionPointResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Ponto de Coleta por ID",
            description = "Retorna os dados de um Ponto de Coleta específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ponto de coleta encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionPointResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado")
    })
    public ResponseEntity<CollectionPointResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/books")
    @Operation(
            summary = "Buscar Ponto de Coleta com seus Exemplares",
            description = "Retorna os dados do Ponto de Coleta e a lista de exemplares de livros cadastrados nele."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ponto de coleta e exemplares retornados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionPointDetailDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado")
    })
    public ResponseEntity<CollectionPointDetailDTO> findByIdWithBooks(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByIdWithBooks(id));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Atualizar Ponto de Coleta",
            description = """
                    Atualiza parcialmente os dados de um Ponto de Coleta. Apenas campos enviados serão alterados.

                    **Admin responsável pelo ponto:** pode atualizar nome, endereço, capacidade e status (somente ATIVO=1 ou LOTADO=2).

                    **Admin Raiz:** pode atualizar todos os campos, incluindo o administrador responsável e o status (ATIVO=1, LOTADO=2 ou INATIVO=3)."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ponto de coleta atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionPointResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN | Admin autenticado não é o responsável por este ponto de coleta | Admin tentou definir status INATIVO"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado | O 'userAdminId' informado não existe ou está inativo (somente Admin Raiz)"),
            @ApiResponse(responseCode = "422", description = "Status APAGADO não é permitido via este endpoint | O 'userAdminId' não possui ROLE_ADMIN | O 'userAdminId' já é responsável por outro ponto de coleta (somente Admin Raiz)")
    })
    public ResponseEntity<CollectionPointResponseDTO> update(@PathVariable Integer id,
                                                             @RequestBody CollectionPointUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "ADMIN RAIZ - Remover Ponto de Coleta",
            description = "Realiza a exclusão lógica de um Ponto de Coleta, marcando-o como APAGADO. Endpoint disponível somente para o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ponto de coleta removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador Raiz"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
