package com.backend.circuler.service;

import com.backend.circuler.dto.user.UserCreateDTO;
import com.backend.circuler.dto.user.UserResponseDTO;
import com.backend.circuler.dto.user.UserUpdateDTO;
import com.backend.circuler.entity.Role;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.exception.NotFoundException;
import com.backend.circuler.exception.UnprocessableEntityException;
import com.backend.circuler.mapper.UserMapper;
import com.backend.circuler.repository.RoleRepository;
import com.backend.circuler.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final UserMapper mapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;
    private final ImageStorageService imageStorageService;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthorizationService authorizationService,
                       ImageStorageService imageStorageService) {
        this.repository = repository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
        this.imageStorageService = imageStorageService;
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO request) {
        Optional<User> byEmail = repository.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            log.warn("Cadastro rejeitado: e-mail já em uso - conflito={}", byEmail.get().getId());
            throw new UnprocessableEntityException("Este e-mail já está em uso.");
        }

        Optional<User> byCpf = repository.findByCpf(request.getCpf());
        if (byCpf.isPresent()) {
            log.warn("Cadastro rejeitado: CPF já cadastrado - conflito={}", byCpf.get().getId());
            throw new UnprocessableEntityException("Este CPF já está cadastrado.");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> {
                    log.error("Role ROLE_USER ausente no banco — verifique o script de inicialização.");
                    return new NotFoundException("Role ROLE_USER não encontrada.");
                });

        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(userRole);

        User savedUser = repository.save(user);
        log.info("Usuário cadastrado - id={} roles=[ROLE_USER]", savedUser.getId());
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
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado ou inativo."));
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
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado ou inativo."));

        List<String> changedFields = new ArrayList<>();

        if (request.getName() != null && !request.getName().isBlank()) {
            existingUser.setName(request.getName());
            changedFields.add("nome");
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!existingUser.getEmail().equals(request.getEmail())) {
                Optional<User> byEmail = repository.findByEmail(request.getEmail());
                if (byEmail.isPresent()) {
                    log.warn("Atualização rejeitada: e-mail já em uso - alvo={} conflito={}",
                            id, byEmail.get().getId());
                    throw new UnprocessableEntityException("Este e-mail já está em uso por outro usuário.");
                }
                existingUser.setEmail(request.getEmail());
                changedFields.add("email");
            }
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            changedFields.add("senha");
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            existingUser.setPhone(request.getPhone());
            changedFields.add("telefone");
        }

        if (request.getStatus() != null) {
            existingUser.setStatus(request.getStatus());
            changedFields.add("status");
        }

        User updatedUser = repository.save(existingUser);
        log.info("Usuário atualizado - ator={} alvo={} campos={}",
                authorizationService.resolveCurrentUser().getId(), id, changedFields);
        return mapper.toDto(updatedUser);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        repository.logicalDeleteById(id, UserStatus.APAGADO);
        log.warn("Usuário apagado logicamente - ator={} alvo={}",
                authorizationService.resolveCurrentUser().getId(), id);
    }

    @Transactional
    public UserResponseDTO updateProfilePicture(MultipartFile file) {
        User currentUser = authorizationService.resolveCurrentUser();

        ImageUploadResult uploaded = imageStorageService.upload(file, ImageStorageService.USERS_FOLDER);

        String previousPublicId = currentUser.getProfilePicturePublicId();
        try {
            imageStorageService.delete(previousPublicId);
        } catch (RuntimeException e) {
            log.warn("Falha ao remover a foto de perfil anterior — asset órfão no Cloudinary - id={} publicId={}",
                    currentUser.getId(), previousPublicId, e);
        }

        currentUser.setProfilePictureUrl(uploaded.getUrl());
        currentUser.setProfilePicturePublicId(uploaded.getPublicId());

        User savedUser = repository.save(currentUser);
        log.info("Foto de perfil atualizada - id={} publicId={}", savedUser.getId(), uploaded.getPublicId());
        return mapper.toDto(savedUser);
    }

    @Transactional
    public UserResponseDTO promoteToAdmin(Integer id) {
        User user = repository.findByIdAndStatusNot(id, UserStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado ou inativo."));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (isAdmin) {
            log.warn("Promoção rejeitada: usuário já é administrador - alvo={}", id);
            throw new UnprocessableEntityException("Usuário já possui a role de administrador.");
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> {
                    log.error("Role ROLE_ADMIN ausente no banco — verifique o script de inicialização.");
                    return new NotFoundException("Role ROLE_ADMIN não encontrada.");
                });

        user.getRoles().add(adminRole);
        User updatedUser = repository.save(user);
        log.warn("Usuário promovido a ROLE_ADMIN - ator={} alvo={}",
                authorizationService.resolveCurrentUser().getId(), id);
        return mapper.toDto(updatedUser);
    }
}
