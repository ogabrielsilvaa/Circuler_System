package com.backend.circuler.controller;

import com.backend.circuler.dto.book.BookCreateDTO;
import com.backend.circuler.dto.book.BookResponseDTO;
import com.backend.circuler.dto.book.BookUpdateDTO;
import com.backend.circuler.service.BookService;
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
@RequestMapping("/api/books")
@Tag(name = "Livros", description = "Endpoints para gerenciamento do catálogo de livros.")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Cadastrar Livro",
            description = "Cadastra um novo livro no catálogo. Endpoint disponível para Administradores e o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Livro cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou em branco no corpo da requisição"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN")
    })
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/pending")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Listar Livros Pendentes de Aprovação",
            description = "Retorna todos os livros com status PENDENTE, criados via doação de livros inéditos. Use este endpoint para visualizar livros que aguardam aprovação para entrar no catálogo global."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN")
    })
    public ResponseEntity<List<BookResponseDTO>> findAllPending() {
        return ResponseEntity.ok(service.findAllPending());
    }

    @PatchMapping("/{id}/approve")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Aprovar Livro Pendente",
            description = "Aprova um livro com status PENDENTE, promovendo-o para ATIVO no catálogo global. Todos os exemplares PENDENTE vinculados a este livro são automaticamente promovidos para DISPONIVEL."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Livro aprovado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "422", description = "Livro não está com status PENDENTE")
    })
    public ResponseEntity<BookResponseDTO> approve(@PathVariable Integer id) {
        return ResponseEntity.ok(service.approve(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar Livros Ativos",
            description = "Retorna todos os livros com status ATIVO cadastrados no catálogo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    public ResponseEntity<List<BookResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Livro por ID",
            description = "Retorna os dados de um livro ativo específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Livro encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<BookResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Atualizar Livro",
            description = "Atualiza parcialmente os dados de um livro. Apenas os campos enviados no corpo da requisição serão alterados. Endpoint disponível para Administradores e o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<BookResponseDTO> update(@PathVariable Integer id,
                                                   @RequestBody BookUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "ADMIN RAIZ - Remover Livro",
            description = "Realiza a exclusão lógica de um livro, marcando-o como APAGADO. Endpoint disponível somente para o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador Raiz"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
