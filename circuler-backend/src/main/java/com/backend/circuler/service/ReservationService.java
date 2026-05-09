package com.backend.circuler.service;

import com.backend.circuler.dto.reservation.ReservationCreateDTO;
import com.backend.circuler.dto.reservation.ReservationResponseDTO;
import com.backend.circuler.dto.reservation.ReservationUpdateDTO;
import com.backend.circuler.entity.BookInstance;
import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.entity.Reservation;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.BookInstanceStatus;
import com.backend.circuler.enums.CollectionPointStatus;
import com.backend.circuler.enums.ReservationStatus;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.exception.ForbiddenException;
import com.backend.circuler.exception.NotFoundException;
import com.backend.circuler.exception.UnprocessableEntityException;
import com.backend.circuler.mapper.ReservationMapper;
import com.backend.circuler.repository.BookInstanceRepository;
import com.backend.circuler.repository.CollectionPointRepository;
import com.backend.circuler.repository.ReservationRepository;
import com.backend.circuler.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final BookInstanceRepository bookInstanceRepository;
    private final CollectionPointRepository collectionPointRepository;
    private final UserRepository userRepository;
    private final ReservationMapper mapper;

    public ReservationService(ReservationRepository repository,
                               BookInstanceRepository bookInstanceRepository,
                               CollectionPointRepository collectionPointRepository,
                               UserRepository userRepository,
                               ReservationMapper mapper) {
        this.repository = repository;
        this.bookInstanceRepository = bookInstanceRepository;
        this.collectionPointRepository = collectionPointRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ReservationResponseDTO create(ReservationCreateDTO request) {
        User currentUser = resolveCurrentUser();

        BookInstance bookInstance = bookInstanceRepository
                .findByIdAndStatusNot(request.getBookInstanceId(), BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado."));

        if (bookInstance.getStatus() != BookInstanceStatus.DISPONIVEL) {
            throw new UnprocessableEntityException("O exemplar não está disponível para reserva.");
        }

        if (repository.existsByBookInstanceIdAndStatus(bookInstance.getId(), ReservationStatus.ATIVA)) {
            throw new UnprocessableEntityException("Já existe uma reserva ativa para este exemplar.");
        }

        String verificationCode = String.format("%04d", new Random().nextInt(10000));

        Reservation reservation = new Reservation();
        reservation.setUser(currentUser);
        reservation.setBookInstance(bookInstance);
        reservation.setVerificationCode(verificationCode);
        reservation.setStatus(ReservationStatus.ATIVA);

        Reservation saved = repository.save(reservation);

        bookInstanceRepository.logicalDeleteById(bookInstance.getId(), BookInstanceStatus.RESERVADO);

        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAllGlobal() {
        return repository.findAllByStatusNot(ReservationStatus.APAGADA)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAllByMyPoint() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CollectionPoint point = collectionPointRepository
                .findByUserAdminEmailAndStatusNot(email, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new UnprocessableEntityException("Você não é responsável por nenhum ponto de coleta ativo."));

        return repository.findAllByBookInstanceCollectionPointIdAndStatusNot(point.getId(), ReservationStatus.APAGADA)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAllMyOwn() {
        User currentUser = resolveCurrentUser();
        return repository.findAllByUserIdAndStatusNot(currentUser.getId(), ReservationStatus.APAGADA)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Integer id) {
        Reservation reservation = repository.findByIdAndStatusNot(id, ReservationStatus.APAGADA)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada."));

        checkReadOwnership(reservation);

        return mapper.toDto(reservation);
    }

    @Transactional
    public ReservationResponseDTO update(Integer id, ReservationUpdateDTO request) {
        Reservation reservation = repository.findByIdAndStatusNot(id, ReservationStatus.APAGADA)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada."));

        if (request.getStatus() == null) {
            return mapper.toDto(reservation);
        }

        if (request.getStatus() == ReservationStatus.ATIVA || request.getStatus() == ReservationStatus.APAGADA) {
            throw new UnprocessableEntityException("Status inválido. Use 2 (CONCLUIDA) ou 3 (CANCELADA).");
        }

        if (reservation.getStatus() != ReservationStatus.ATIVA) {
            throw new UnprocessableEntityException("Somente reservas ativas podem ser atualizadas.");
        }

        reservation.setStatus(request.getStatus());
        Reservation saved = repository.save(reservation);

        if (request.getStatus() == ReservationStatus.CONCLUIDA) {
            bookInstanceRepository.logicalDeleteById(reservation.getBookInstance().getId(), BookInstanceStatus.RETIRADO);
        } else if (request.getStatus() == ReservationStatus.CANCELADA) {
            bookInstanceRepository.logicalDeleteById(reservation.getBookInstance().getId(), BookInstanceStatus.DISPONIVEL);
        }

        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Integer id) {
        Reservation reservation = repository.findByIdAndStatusNot(id, ReservationStatus.APAGADA)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada."));

        repository.logicalDeleteById(reservation.getId(), ReservationStatus.APAGADA);
    }

    private User resolveCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndStatusNot(email, UserStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    private void checkReadOwnership(Reservation reservation) {
        User currentUser = resolveCurrentUser();

        if (hasRole(currentUser, "ROLE_ROOT_ADMIN")) {
            return;
        }

        if (hasRole(currentUser, "ROLE_ADMIN")) {
            String adminEmail = currentUser.getEmail();
            String pointAdminEmail = reservation.getBookInstance().getCollectionPoint().getUserAdmin().getEmail();
            if (adminEmail.equals(pointAdminEmail)) {
                return;
            }
        }

        if (reservation.getUser().getId().equals(currentUser.getId())) {
            return;
        }

        throw new ForbiddenException("Você não tem permissão para acessar esta reserva.");
    }
}
