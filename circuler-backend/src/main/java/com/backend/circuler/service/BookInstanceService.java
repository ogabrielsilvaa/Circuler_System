package com.backend.circuler.service;

import com.backend.circuler.dto.bookinstance.BookInstanceCreateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceResponseDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceUpdateDTO;
import com.backend.circuler.entity.Book;
import com.backend.circuler.entity.BookInstance;
import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.BookInstanceStatus;
import com.backend.circuler.enums.BookStatus;
import com.backend.circuler.enums.CollectionPointStatus;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.mapper.BookInstanceMapper;
import com.backend.circuler.repository.BookInstanceRepository;
import com.backend.circuler.repository.BookRepository;
import com.backend.circuler.repository.CollectionPointRepository;
import com.backend.circuler.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookInstanceService {

    private final BookInstanceRepository repository;
    private final BookInstanceMapper mapper;
    private final BookRepository bookRepository;
    private final CollectionPointRepository collectionPointRepository;
    private final UserRepository userRepository;

    public BookInstanceService(BookInstanceRepository repository,
                               BookInstanceMapper mapper,
                               BookRepository bookRepository,
                               CollectionPointRepository collectionPointRepository,
                               UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.collectionPointRepository = collectionPointRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookInstanceResponseDTO createForPoint(Integer pointId, BookInstanceCreateDTO request) {
        CollectionPoint point = collectionPointRepository.findByIdAndStatusNot(pointId, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Ponto de coleta não encontrado."));

        return doCreate(point, request);
    }

    @Transactional
    public BookInstanceResponseDTO createForMyPoint(BookInstanceCreateDTO request) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        CollectionPoint point = collectionPointRepository.findByUserAdminEmailAndStatusNot(currentEmail, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Você não é responsável por nenhum ponto de coleta ativo."));

        return doCreate(point, request);
    }

    private BookInstanceResponseDTO doCreate(CollectionPoint point, BookInstanceCreateDTO request) {
        long activeCount = repository.countByCollectionPointIdAndStatusNot(point.getId(), BookInstanceStatus.APAGADO);
        if (activeCount >= point.getCapacityLimit()) {
            throw new RuntimeException("Ponto de coleta atingiu a capacidade máxima de " + point.getCapacityLimit() + " exemplares.");
        }

        Book book = bookRepository.findByIdAndStatusNot(request.getBookId(), BookStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado ou inativo."));

        User donor = null;
        if (request.getUserDonorId() != null) {
            donor = userRepository.findByIdAndStatusNot(request.getUserDonorId(), UserStatus.APAGADO)
                    .orElseThrow(() -> new RuntimeException("Usuário doador não encontrado ou inativo."));
        }

        BookInstance instance = new BookInstance();
        instance.setBook(book);
        instance.setCollectionPoint(point);
        instance.setUserDonor(donor);
        instance.setStatus(BookInstanceStatus.DISPONIVEL);

        return mapper.toDto(repository.save(instance));
    }

    public List<BookInstanceResponseDTO> findAll() {
        return repository.findAllByStatusNot(BookInstanceStatus.APAGADO)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public BookInstanceResponseDTO findById(Integer id) {
        BookInstance instance = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado."));
        return mapper.toDto(instance);
    }

    @Transactional
    public BookInstanceResponseDTO update(Integer id, BookInstanceUpdateDTO request) {
        BookInstance existing = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado."));

        checkOwnership(existing);

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Integer id) {
        BookInstance existing = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado."));

        checkOwnership(existing);

        repository.logicalDeleteById(id, BookInstanceStatus.APAGADO);
    }

    private void checkOwnership(BookInstance instance) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailAndStatusNot(currentEmail, UserStatus.APAGADO)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));

        boolean isRootAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> "ROLE_ROOT_ADMIN".equals(role.getName()));

        if (!isRootAdmin && !instance.getCollectionPoint().getUserAdmin().getEmail().equals(currentEmail)) {
            throw new RuntimeException("Você não tem permissão para gerenciar exemplares deste ponto de coleta.");
        }
    }
}