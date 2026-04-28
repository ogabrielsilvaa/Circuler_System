package com.backend.circuler.mapper;

import com.backend.circuler.dto.book.BookCreateDTO;
import com.backend.circuler.dto.book.BookResponseDTO;
import com.backend.circuler.entity.Book;
import com.backend.circuler.enums.BookStatus;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookCreateDTO createDTO) {
        Book book = new Book();
        book.setTitle(createDTO.getTitle());
        book.setAuthor(createDTO.getAuthor());
        book.setPublisher(createDTO.getPublisher());
        book.setThumbnailUrl(createDTO.getThumbnailUrl());
        book.setCategory(createDTO.getCategory());
        book.setIsbn(createDTO.getIsbn());
        book.setStatus(BookStatus.ATIVO);
        return book;
    }

    public BookResponseDTO toDto(Book entity) {
        return new BookResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getPublisher(),
                entity.getThumbnailUrl(),
                entity.getCategory(),
                entity.getIsbn(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
