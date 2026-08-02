package com.backend.circuler.controller;

import com.backend.circuler.dto.auth.LoginRequestDTO;
import com.backend.circuler.dto.auth.LoginResponseDTO;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.repository.UserRepository;
import com.backend.circuler.security.CustomUserDetailsService;
import com.backend.circuler.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação na API.")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar Usuário",
            description = "Autentica um usuário com e-mail e senha e retorna um token JWT para uso nas demais requisições. Endpoint público — não requer autenticação prévia."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login efetuado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou em branco no corpo da requisição"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas — e-mail ou senha incorretos")
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            logAuthenticationFailure(request.getEmail());
            throw e;
        }

        User user = userDetailsService.loadUserEntityByUsername(request.getEmail());

        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(r -> new org.springframework.security.core.authority.SimpleGrantedAuthority(r.getName()))
                                .collect(Collectors.toList())
                )
        );

        List<String> roles = user.getRoles().stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());

        log.info("Login efetuado - id={} roles={}", user.getId(), roles);

        return ResponseEntity.ok(new LoginResponseDTO(user.getId(), token, user.getEmail(), roles));
    }

    /**
     * Registra a falha de login distinguindo senha incorreta de conta inexistente.
     *
     * A distinção fica só no log — a resposta ao cliente é idêntica nos dois casos,
     * para não permitir enumeração de usuários.
     *
     * Quando a conta existe, registra o id: é resolvível no banco quando for preciso
     * agir, sem espalhar e-mail pelo log. Quando não existe, não há id nem nada a
     * agir — o que importa é a contagem de ocorrências.
     */
    private void logAuthenticationFailure(String email) {
        userRepository.findByEmailAndStatusNot(email, UserStatus.APAGADO)
                .ifPresentOrElse(
                        user -> log.warn("Falha de autenticação motivo=SENHA_INCORRETA - alvo={}", user.getId()),
                        () -> log.warn("Falha de autenticação motivo=USUARIO_INEXISTENTE")
                );
    }
}
