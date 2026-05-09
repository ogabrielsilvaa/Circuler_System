package com.backend.circuler.controller;

import com.backend.circuler.dto.reservation.ReservationCreateDTO;
import com.backend.circuler.dto.reservation.ReservationResponseDTO;
import com.backend.circuler.dto.reservation.ReservationUpdateDTO;
import com.backend.circuler.service.ReservationService;
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
@RequestMapping("/api/reservations")
@Tag(name = "Reservas", description = "Gerenciamento de reservas de exemplares")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Criar reserva",
            description = "Cria uma reserva para um exemplar com status DISPONIVEL. Gera um código de verificação de 4 dígitos. Atualiza o status do exemplar para RESERVADO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado"),
            @ApiResponse(responseCode = "422", description = "Exemplar não está disponível | Já existe reserva ativa para este exemplar")
    })
    public ResponseEntity<ReservationResponseDTO> create(@Valid @RequestBody ReservationCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/all")
    @Operation(
            summary = "ROOT_ADMIN - Listar todas as reservas do sistema",
            description = "Retorna todas as reservas não deletadas de todos os pontos de coleta."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ROOT_ADMIN")
    })
    public ResponseEntity<List<ReservationResponseDTO>> findAllGlobal() {
        return ResponseEntity.ok(service.findAllGlobal());
    }

    @GetMapping("/my-point")
    @Operation(
            summary = "ADMIN - Listar reservas do meu ponto de coleta",
            description = "Retorna todas as reservas não deletadas dos exemplares do ponto de coleta administrado pelo usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "422", description = "Admin não é responsável por nenhum ponto de coleta ativo")
    })
    public ResponseEntity<List<ReservationResponseDTO>> findAllByMyPoint() {
        return ResponseEntity.ok(service.findAllByMyPoint());
    }

    @GetMapping("/my-reservations")
    @Operation(
            summary = "Listar minhas reservas",
            description = "Retorna todas as reservas não deletadas do usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<ReservationResponseDTO>> findAllMyOwn() {
        return ResponseEntity.ok(service.findAllMyOwn());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Retorna uma reserva pelo ID. Usuário comum só pode acessar suas próprias reservas; ADMIN acessa as do seu ponto; ROOT_ADMIN acessa qualquer uma."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar esta reserva"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<ReservationResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "ADMIN - Atualizar status da reserva",
            description = "Atualiza o status de uma reserva ativa. CONCLUIDA (2): exemplar passa para RETIRADO. CANCELADA (3): exemplar volta para DISPONIVEL."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
            @ApiResponse(responseCode = "422", description = "Status inválido | Reserva não está ativa")
    })
    public ResponseEntity<ReservationResponseDTO> update(@PathVariable Integer id,
                                                          @RequestBody ReservationUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "ROOT_ADMIN - Deletar reserva",
            description = "Delete lógico da reserva (status APAGADA). Não altera o status do exemplar — use PATCH para cancelar corretamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva deletada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ROOT_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
