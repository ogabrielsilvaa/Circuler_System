package com.backend.circuler.service;

import com.backend.circuler.dto.book.BookCreateDTO;
import com.backend.circuler.dto.book.BookResponseDTO;
import com.backend.circuler.dto.book.BookUpdateDTO;
import com.backend.circuler.entity.Book;
import com.backend.circuler.enums.BookInstanceStatus;
import com.backend.circuler.enums.BookStatus;
import com.backend.circuler.exception.NotFoundException;
import com.backend.circuler.exception.UnprocessableEntityException;
import com.backend.circuler.mapper.BookMapper;
import com.backend.circuler.repository.BookInstanceRepository;
import com.backend.circuler.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;
    private final BookInstanceRepository bookInstanceRepository;

    public BookService(BookRepository repository, BookMapper mapper, BookInstanceRepository bookInstanceRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.bookInstanceRepository = bookInstanceRepository;
    }

    @Transactional
    public BookResponseDTO create(BookCreateDTO request) {
        Book book = mapper.toEntity(request);
        Book savedBook = repository.save(book);
        return mapper.toDto(savedBook);
    }

    public List<BookResponseDTO> findAllActive() {
        return repository.findAllByStatus(BookStatus.ATIVO)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookResponseDTO> findAllPending() {
        return repository.findAllByStatus(BookStatus.PENDENTE)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public BookResponseDTO findById(Integer id) {
        Book book = repository.findByIdAndStatusNot(id, BookStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));
        return mapper.toDto(book);
    }

    @Transactional
    public BookResponseDTO update(Integer id, BookUpdateDTO request) {
        Book existingBook = repository.findByIdAndStatusNot(id, BookStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            existingBook.setTitle(request.getTitle());
        }

        if (request.getAuthor() != null && !request.getAuthor().isBlank()) {
            existingBook.setAuthor(request.getAuthor());
        }

        if (request.getPublisher() != null && !request.getPublisher().isBlank()) {
            existingBook.setPublisher(request.getPublisher());
        }

        if (request.getThumbnailUrl() != null && !request.getThumbnailUrl().isBlank()) {
            existingBook.setThumbnailUrl(request.getThumbnailUrl());
        }

        if (request.getCategory() != null) {
            existingBook.setCategory(request.getCategory());
        }

        if (request.getIsbn() != null && !request.getIsbn().isBlank()) {
            existingBook.setIsbn(request.getIsbn());
        }

        if (request.getStatus() != null) {
            existingBook.setStatus(request.getStatus());
        }

        Book updatedBook = repository.save(existingBook);
        return mapper.toDto(updatedBook);
    }

    @Transactional
    public BookResponseDTO approve(Integer id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));

        if (book.getStatus() != BookStatus.PENDENTE) {
            throw new UnprocessableEntityException("Apenas livros com status PENDENTE podem ser aprovados.");
        }

        book.setStatus(BookStatus.ATIVO);
        repository.save(book);

        bookInstanceRepository.updateStatusByBookIdAndStatus(id, BookInstanceStatus.PENDENTE, BookInstanceStatus.DISPONIVEL);

        return mapper.toDto(book);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Livro não encontrado.");
        }
        repository.logicalDeleteById(id, BookStatus.APAGADO);
    }
}
