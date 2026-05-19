package com.backend.circuler.controller;

import com.backend.circuler.dto.bookinstance.BookInstanceApproveDonationDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceCreateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceDonateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceNewBookCreateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceResponseDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceUpdateDTO;
import com.backend.circuler.enums.BookCategory;
import com.backend.circuler.service.BookInstanceService;
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
@RequestMapping("/api/book-instances")
@Tag(name = "Exemplares", description = "Endpoints para gerenciamento de exemplares de livros nos pontos de coleta.")
public class BookInstanceController {

    private final BookInstanceService service;

    public BookInstanceController(BookInstanceService service) {
        this.service = service;
    }

    @PostMapping("/donate")
    @Operation(
            summary = "Doar um livro novo para um Ponto de Coleta",
            description = """
                    Permite que qualquer usuário autenticado doe um livro novo para um Ponto de Coleta ativo.
                    O livro é criado com status PENDENTE e o exemplar também fica PENDENTE até que o Admin responsável pelo ponto o aprove via PATCH /api/book-instances/{id}."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Doação registrada com sucesso — livro e exemplar criados com status PENDENTE",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou em branco"),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado"),
            @ApiResponse(responseCode = "422", description = "Ponto de coleta não está ativo | Ponto de coleta atingiu a capacidade máxima")
    })
    public ResponseEntity<BookInstanceResponseDTO> donate(@Valid @RequestBody BookInstanceDonateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.donate(request));
    }

    @PostMapping("/point/{pointId}")
    @Operation(
            summary = "ADMIN RAIZ - Registrar exemplar em qualquer Ponto de Coleta",
            description = "Registra um exemplar de um livro existente no catálogo em um Ponto de Coleta específico. Endpoint disponível somente para o Administrador Raiz do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Exemplar registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador Raiz"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado | Livro não encontrado ou inativo | Usuário doador não encontrado"),
            @ApiResponse(responseCode = "422", description = "Ponto de coleta atingiu a capacidade máxima")
    })
    public ResponseEntity<BookInstanceResponseDTO> createForPoint(
            @PathVariable Integer pointId,
            @RequestBody BookInstanceCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createForPoint(pointId, request));
    }

    @PostMapping("/my-point")
    @Operation(
            summary = "ADMIN - Registrar exemplar no meu Ponto de Coleta",
            description = "Registra um exemplar de um livro já existente no catálogo global no Ponto de Coleta administrado pelo usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Exemplar registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado ou inativo | Usuário doador não encontrado"),
            @ApiResponse(responseCode = "422", description = "Admin não é responsável por nenhum ponto ativo | Ponto de coleta atingiu a capacidade máxima")
    })
    public ResponseEntity<BookInstanceResponseDTO> createForMyPoint(
            @RequestBody BookInstanceCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createForMyPoint(request));
    }

    @PostMapping("/my-point/new-book")
    @Operation(
            summary = "ADMIN - Registrar exemplar de livro inédito no catálogo",
            description = """
                    Registra a doação de um livro que ainda não existe no catálogo global.
                    O livro é criado com status PENDENTE e o exemplar também fica PENDENTE até que o livro seja aprovado.
                    Após a aprovação via PATCH /api/books/{id}/approve, o livro passa a ATIVO e o exemplar a DISPONIVEL."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Livro PENDENTE e exemplar PENDENTE criados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios do livro ausentes ou em branco"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuário doador não encontrado ou inativo"),
            @ApiResponse(responseCode = "422", description = "Admin não é responsável por nenhum ponto ativo | Ponto de coleta atingiu a capacidade máxima")
    })
    public ResponseEntity<BookInstanceResponseDTO> createForMyPointWithNewBook(
            @Valid @RequestBody BookInstanceNewBookCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createForMyPointWithNewBook(request));
    }

    @GetMapping("/pending")
    @Operation(
            summary = "ADMIN RAIZ - Listar todos os Exemplares PENDENTE",
            description = "Retorna todos os exemplares com status PENDENTE de todos os Pontos de Coleta do sistema. Endpoint disponível somente para o Administrador Raiz."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado não é o Administrador Raiz")
    })
    public ResponseEntity<List<BookInstanceResponseDTO>> findAllPending() {
        return ResponseEntity.ok(service.findAllPending());
    }

    @GetMapping("/my-point/pending")
    @Operation(
            summary = "ADMIN - Listar exemplares PENDENTE do meu Ponto de Coleta",
            description = "Retorna todos os exemplares com status PENDENTE vinculados ao Ponto de Coleta administrado pelo usuário autenticado. Use este endpoint para visualizar doações de livros inéditos aguardando aprovação."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "422", description = "Admin não é responsável por nenhum ponto de coleta ativo")
    })
    public ResponseEntity<List<BookInstanceResponseDTO>> findPendingForMyPoint() {
        return ResponseEntity.ok(service.findPendingForMyPoint());
    }

    @GetMapping("/my-point")
    @Operation(
            summary = "ADMIN - Listar todos os Exemplares do meu Ponto de Coleta",
            description = """
                    Perspectiva de gestão: retorna todos os exemplares vinculados ao Ponto de Coleta \
                    administrado pelo usuário autenticado, com qualquer status ativo — DISPONIVEL, RESERVADO, \
                    RETIRADO e PENDENTE. Exemplares excluídos (APAGADO) são omitidos. \
                    Use este endpoint para ter visibilidade completa do acervo do seu ponto, \
                    incluindo exemplares em trânsito ou aguardando aprovação.
                    Para o catálogo público de exemplares disponíveis para reserva, \
                    use GET /api/book-instances."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN"),
            @ApiResponse(responseCode = "422", description = "Admin não é responsável por nenhum ponto de coleta ativo")
    })
    public ResponseEntity<List<BookInstanceResponseDTO>> findAllForMyPoint() {
        return ResponseEntity.ok(service.findAllForMyPoint());
    }

    @GetMapping
    @Operation(
            summary = "Listar Exemplares disponíveis (catálogo público)",
            description = """
                    Perspectiva de catálogo: retorna todos os exemplares com status DISPONIVEL ou RESERVADO \
                    de todos os Pontos de Coleta do sistema, visíveis para qualquer usuário autenticado. \
                    Exemplares com status APAGADO, RETIRADO e PENDENTE são omitidos intencionalmente — \
                    eles não estão acessíveis para reserva.
                    Para visualizar todos os exemplares do seu ponto (incluindo RETIRADO e PENDENTE), \
                    use GET /api/book-instances/my-point (requer ROLE_ADMIN)."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    public ResponseEntity<List<BookInstanceResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Exemplar por ID",
            description = "Retorna os dados de um exemplar específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exemplar encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado")
    })
    public ResponseEntity<BookInstanceResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Buscar Exemplares por título do livro",
            description = "Retorna todos os exemplares cujo título do livro contém o termo informado. Busca parcial e insensível a maiúsculas/minúsculas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    public ResponseEntity<List<BookInstanceResponseDTO>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(service.searchByTitle(title));
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filtrar Exemplares por categoria do livro",
            description = "Retorna todos os exemplares cujo livro pertence à categoria informada. Categorias: 1=INFANTIL_JUVENIL, 2=AUTOAJUDA, 3=DIDATICO, 4=ESCOLAR."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "400", description = "Código de categoria inválido")
    })
    public ResponseEntity<List<BookInstanceResponseDTO>> filterByCategory(@RequestParam Integer category) {
        return ResponseEntity.ok(service.filterByCategory(BookCategory.fromCode(category)));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Atualizar Exemplar",
            description = """
                    Atualiza campos de um exemplar e do livro associado. Campos disponíveis: status (BookInstanceStatus), bookThumbnailUrl, bookIsbn.
                    Todos os campos são opcionais — apenas os campos não-nulos serão atualizados.
                    O Admin Raiz pode atualizar qualquer exemplar; o Admin comum somente os do seu ponto de coleta."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exemplar atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN | Admin não é responsável por este exemplar"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado")
    })
    public ResponseEntity<BookInstanceResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody BookInstanceUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/approve-donation")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Aprovar doação de Exemplar",
            description = """
                    Aprova um exemplar doado por um usuário comum, movendo seu status de PENDENTE para DISPONIVEL.
                    Permite informar opcionalmente a URL da capa (bookThumbnailUrl) e o ISBN do livro.
                    O Admin comum só pode aprovar exemplares do seu ponto de coleta."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Doação aprovada com sucesso — exemplar agora DISPONIVEL",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookInstanceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN | Admin não é responsável por este exemplar"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado"),
            @ApiResponse(responseCode = "422", description = "Exemplar não está com status PENDENTE")
    })
    public ResponseEntity<BookInstanceResponseDTO> approveDonation(
            @PathVariable Integer id,
            @RequestBody BookInstanceApproveDonationDTO request) {
        return ResponseEntity.ok(service.approveDonation(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "ADMIN / ADMIN RAIZ - Remover Exemplar",
            description = "Realiza a exclusão lógica de um exemplar, marcando-o como APAGADO. O Admin Raiz pode remover qualquer exemplar; o Admin comum somente os do seu ponto de coleta. Se houver uma reserva deste exemplar, ela será cancelada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exemplar removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui ROLE_ADMIN | Admin não é responsável por este exemplar"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
