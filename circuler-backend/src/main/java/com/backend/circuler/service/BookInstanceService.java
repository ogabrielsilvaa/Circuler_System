package com.backend.circuler.service;

import com.backend.circuler.dto.bookinstance.BookInstanceApproveDonationDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceCreateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceDonateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceNewBookCreateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceResponseDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceUpdateDTO;
import com.backend.circuler.entity.Book;
import com.backend.circuler.entity.BookInstance;
import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.BookCategory;
import com.backend.circuler.enums.BookInstanceStatus;
import com.backend.circuler.enums.BookStatus;
import com.backend.circuler.enums.CollectionPointStatus;
import com.backend.circuler.enums.ReservationStatus;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.exception.NotFoundException;
import com.backend.circuler.exception.UnprocessableEntityException;
import com.backend.circuler.mapper.BookInstanceMapper;
import com.backend.circuler.repository.BookInstanceRepository;
import com.backend.circuler.repository.BookRepository;
import com.backend.circuler.repository.CollectionPointRepository;
import com.backend.circuler.repository.ReservationRepository;
import com.backend.circuler.repository.UserRepository;
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
    private final ReservationRepository reservationRepository;
    private final AuthorizationService authorizationService;

    public BookInstanceService(BookInstanceRepository repository,
                               BookInstanceMapper mapper,
                               BookRepository bookRepository,
                               CollectionPointRepository collectionPointRepository,
                               UserRepository userRepository,
                               ReservationRepository reservationRepository,
                               AuthorizationService authorizationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.collectionPointRepository = collectionPointRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public BookInstanceResponseDTO createForPoint(Integer pointId, BookInstanceCreateDTO request) {
        CollectionPoint point = collectionPointRepository.findByIdAndStatusNot(pointId, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));

        return doCreate(point, request);
    }

    @Transactional
    public BookInstanceResponseDTO createForMyPoint(BookInstanceCreateDTO request) {
        CollectionPoint point = resolveMyPoint();
        return doCreate(point, request);
    }

    @Transactional
    public BookInstanceResponseDTO createForMyPointWithNewBook(BookInstanceNewBookCreateDTO request) {
        CollectionPoint point = resolveMyPoint();
        checkCapacity(point);

        Book book = new Book();
        book.setTitle(request.getBookTitle());
        book.setAuthor(request.getBookAuthor());
        book.setPublisher(request.getBookPublisher());
        book.setThumbnailUrl(request.getBookThumbnailUrl());
        book.setCategory(request.getBookCategory());
        book.setIsbn(request.getBookIsbn());
        book.setStatus(BookStatus.PENDENTE);
        book = bookRepository.save(book);

        User donor = resolveDonor(request.getUserDonorId());

        BookInstance instance = new BookInstance();
        instance.setBook(book);
        instance.setCollectionPoint(point);
        instance.setUserDonor(donor);
        instance.setStatus(BookInstanceStatus.PENDENTE);

        return mapper.toDto(repository.save(instance));
    }

    @Transactional
    public BookInstanceResponseDTO donate(BookInstanceDonateDTO request) {
        CollectionPoint point = collectionPointRepository
                .findByIdAndStatusNot(request.getCollectionPointId(), CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));

        if (point.getStatus() != CollectionPointStatus.ATIVO) {
            throw new UnprocessableEntityException("O ponto de coleta não está aceitando doações no momento.");
        }

        checkCapacity(point);

        String currentEmail = authorizationService.resolveCurrentEmail();
        User donor = userRepository.findByEmailAndStatusNot(currentEmail, UserStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        Book book = new Book();
        book.setTitle(request.getBookTitle());
        book.setAuthor(request.getBookAuthor());
        book.setPublisher(request.getBookPublisher());
        book.setThumbnailUrl(request.getBookThumbnailUrl());
        book.setCategory(request.getBookCategory());
        book.setIsbn(request.getBookIsbn());
        book.setStatus(BookStatus.PENDENTE);
        book = bookRepository.save(book);

        BookInstance instance = new BookInstance();
        instance.setBook(book);
        instance.setCollectionPoint(point);
        instance.setUserDonor(donor);
        instance.setStatus(BookInstanceStatus.PENDENTE);

        return mapper.toDto(repository.save(instance));
    }

    public List<BookInstanceResponseDTO> findAllPending() {
        return repository.findAllByStatus(BookInstanceStatus.PENDENTE)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookInstanceResponseDTO> findPendingForMyPoint() {
        CollectionPoint point = resolveMyPoint();
        return repository.findAllByCollectionPointIdAndStatus(point.getId(), BookInstanceStatus.PENDENTE)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookInstanceResponseDTO> findAllForMyPoint() {
        CollectionPoint point = resolveMyPoint();
        return repository.findAllByCollectionPointIdAndStatusNot(point.getId(), BookInstanceStatus.APAGADO)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private BookInstanceResponseDTO doCreate(CollectionPoint point, BookInstanceCreateDTO request) {
        checkCapacity(point);

        Book book = bookRepository.findByIdAndStatusNot(request.getBookId(), BookStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado ou inativo."));

        User donor = resolveDonor(request.getUserDonorId());

        BookInstance instance = new BookInstance();
        instance.setBook(book);
        instance.setCollectionPoint(point);
        instance.setUserDonor(donor);
        instance.setStatus(BookInstanceStatus.DISPONIVEL);

        return mapper.toDto(repository.save(instance));
    }

    private CollectionPoint resolveMyPoint() {
        String currentEmail = authorizationService.resolveCurrentEmail();
        return collectionPointRepository.findByUserAdminEmailAndStatusNot(currentEmail, CollectionPointStatus.APAGADO)
                .orElseThrow(() -> new UnprocessableEntityException("Você não é responsável por nenhum ponto de coleta ativo."));
    }

    private void checkCapacity(CollectionPoint point) {
        long activeCount = repository.countByCollectionPointIdAndStatusNot(point.getId(), BookInstanceStatus.APAGADO);
        if (activeCount >= point.getCapacityLimit()) {
            throw new UnprocessableEntityException("Ponto de coleta atingiu a capacidade máxima de " + point.getCapacityLimit() + " exemplares.");
        }
    }

    private User resolveDonor(Integer userDonorId) {
        if (userDonorId == null) return null;
        return userRepository.findByIdAndStatusNot(userDonorId, UserStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Usuário doador não encontrado ou inativo."));
    }

    public List<BookInstanceResponseDTO> findAll() {
        return repository.findAllByStatusNotIn(
                        List.of(BookInstanceStatus.APAGADO, BookInstanceStatus.RETIRADO, BookInstanceStatus.PENDENTE))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public BookInstanceResponseDTO findById(Integer id) {
        BookInstance instance = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado."));
        return mapper.toDto(instance);
    }

    public List<BookInstanceResponseDTO> searchByTitle(String title) {
        return repository.findByBookTitleContainingIgnoreCaseAndStatusNotIn(
                        title,
                        List.of(BookInstanceStatus.APAGADO, BookInstanceStatus.RETIRADO, BookInstanceStatus.PENDENTE))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookInstanceResponseDTO> filterByCategory(BookCategory category) {
        return repository.findByBookCategoryAndStatusNotIn(
                        category,
                        List.of(BookInstanceStatus.APAGADO, BookInstanceStatus.RETIRADO, BookInstanceStatus.PENDENTE))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookInstanceResponseDTO update(Integer id, BookInstanceUpdateDTO request) {
        BookInstance existing = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado."));

        authorizationService.assertCanManageBookInstance(existing);

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        Book book = existing.getBook();
        if (request.getBookThumbnailUrl() != null) {
            book.setThumbnailUrl(request.getBookThumbnailUrl());
        }
        if (request.getBookIsbn() != null) {
            book.setIsbn(request.getBookIsbn());
        }
        bookRepository.save(book);

        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public BookInstanceResponseDTO approveDonation(Integer id, BookInstanceApproveDonationDTO request) {
        BookInstance existing = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado."));

        authorizationService.assertCanManageBookInstance(existing);

        if (existing.getStatus() != BookInstanceStatus.PENDENTE) {
            throw new UnprocessableEntityException("Este exemplar não está pendente de aprovação.");
        }

        Book book = existing.getBook();
        if (request.getBookThumbnailUrl() != null) {
            book.setThumbnailUrl(request.getBookThumbnailUrl());
        }
        if (request.getBookIsbn() != null) {
            book.setIsbn(request.getBookIsbn());
        }
        bookRepository.save(book);

        existing.setStatus(BookInstanceStatus.DISPONIVEL);
        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Integer id) {
        BookInstance existing = repository.findByIdAndStatusNot(id, BookInstanceStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado."));

        authorizationService.assertCanManageBookInstance(existing);

        reservationRepository.findByBookInstanceIdAndStatus(id, ReservationStatus.ATIVA)
                .ifPresent(reservation ->
                        reservationRepository.logicalDeleteById(reservation.getId(), ReservationStatus.CANCELADA));

        repository.logicalDeleteById(id, BookInstanceStatus.APAGADO);
    }
}
