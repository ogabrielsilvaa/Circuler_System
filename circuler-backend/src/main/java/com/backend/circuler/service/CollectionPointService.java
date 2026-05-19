package com.backend.circuler.service;

import com.backend.circuler.dto.collectionpoint.CollectionPointCreateDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointDetailDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointResponseDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointUpdateDTO;
import com.backend.circuler.entity.BookInstance;
import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.BookInstanceStatus;
import com.backend.circuler.enums.CollectionPointStatus;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.exception.NotFoundException;
import com.backend.circuler.exception.UnprocessableEntityException;
import com.backend.circuler.mapper.CollectionPointMapper;
import com.backend.circuler.repository.BookInstanceRepository;
import com.backend.circuler.repository.CollectionPointRepository;
import com.backend.circuler.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionPointService {

    private final CollectionPointRepository repository;
    private final CollectionPointMapper mapper;
    private final UserRepository userRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final AuthorizationService authorizationService;

    public CollectionPointService(CollectionPointRepository repository,
                                  CollectionPointMapper mapper,
                                  UserRepository userRepository,
                                  BookInstanceRepository bookInstanceRepository,
                                  AuthorizationService authorizationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.bookInstanceRepository = bookInstanceRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public CollectionPointResponseDTO create(CollectionPointCreateDTO request) {
        authorizationService.assertIsRootAdmin();

        User userAdmin = userRepository.findByIdAndStatusNot(request.getUserAdminId(), UserStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Usuário responsável não encontrado ou inativo."));

        boolean isAdmin = userAdmin.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
        if (!isAdmin) {
            throw new UnprocessableEntityException("O usuário responsável deve ser um administrador.");
        }

        if (repository.existsByUserAdminIdAndStatusNot(request.getUserAdminId(), CollectionPointStatus.APAGADO)) {
            throw new UnprocessableEntityException("Este usuário já é responsável por outro ponto de coleta.");
        }

        CollectionPoint point = mapper.toEntity(request, userAdmin);
        CollectionPoint saved = repository.save(point);
        return mapper.toDto(saved);
    }

    public List<CollectionPointResponseDTO> findAllActive() {
        return repository.findAllByStatusNot(CollectionPointStatus.APAGADO)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CollectionPointResponseDTO findById(Integer id) {
        CollectionPoint point = repository.findByIdAndStatusNot(id, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        return mapper.toDto(point);
    }

    public CollectionPointDetailDTO findByIdWithBooks(Integer id) {
        CollectionPoint point = repository.findByIdAndStatusNot(id, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));

        List<BookInstance> instances = bookInstanceRepository
                .findAllByCollectionPointIdAndStatusNotIn(
                        id,
                        List.of(BookInstanceStatus.APAGADO, BookInstanceStatus.RETIRADO, BookInstanceStatus.PENDENTE));

        return mapper.toDetailDto(point, instances);
    }

    @Transactional
    public CollectionPointResponseDTO update(Integer id, CollectionPointUpdateDTO request) {
        CollectionPoint existing = repository.findByIdAndStatusNot(id, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));

        authorizationService.assertCanManageCollectionPoint(existing);
        User currentUser = authorizationService.resolveCurrentUser();
        boolean isRootAdmin = authorizationService.hasRole(currentUser, "ROLE_ROOT_ADMIN");

        if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName());
        }

        if (request.getAddressStreet() != null && !request.getAddressStreet().isBlank()) {
            existing.setAddressStreet(request.getAddressStreet());
        }

        if (request.getAddressNeighborhood() != null && !request.getAddressNeighborhood().isBlank()) {
            existing.setAddressNeighborhood(request.getAddressNeighborhood());
        }

        if (request.getCapacityLimit() != null) {
            existing.setCapacityLimit(request.getCapacityLimit());
        }

        if (isRootAdmin) {
            if (request.getUserAdminId() != null && !request.getUserAdminId().equals(existing.getUserAdmin().getId())) {
                User newAdmin = userRepository.findByIdAndStatusNot(request.getUserAdminId(), UserStatus.APAGADO)
                        .orElseThrow(() -> new NotFoundException("Usuário responsável não encontrado ou inativo."));

                boolean isAdmin = newAdmin.getRoles().stream()
                        .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
                if (!isAdmin) {
                    throw new UnprocessableEntityException("O usuário responsável deve ser um administrador.");
                }

                if (repository.existsByUserAdminIdAndStatusNotAndIdNot(request.getUserAdminId(), CollectionPointStatus.APAGADO, id)) {
                    throw new UnprocessableEntityException("Este usuário já é responsável por outro ponto de coleta.");
                }

                existing.setUserAdmin(newAdmin);
            }
        }

        if (request.getStatus() != null) {
            if (request.getStatus() == CollectionPointStatus.APAGADO) {
                throw new UnprocessableEntityException("Utilize o endpoint de exclusão para apagar um ponto de coleta.");
            }
            if (!isRootAdmin && request.getStatus() == CollectionPointStatus.INATIVO) {
                throw new UnprocessableEntityException("Apenas o administrador raiz pode inativar um ponto de coleta.");
            }
            existing.setStatus(request.getStatus());
        }

        CollectionPoint updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Transactional
    public void delete(Integer id) {
        repository.findByIdAndStatusNot(id, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        repository.logicalDeleteById(id, CollectionPointStatus.APAGADO);
    }
}
