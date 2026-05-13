package com.backend.circuler.mapper;

import com.backend.circuler.dto.bookinstance.BookInstanceResponseDTO;
import com.backend.circuler.entity.BookInstance;
import org.springframework.stereotype.Component;

@Component
public class BookInstanceMapper {

    public BookInstanceResponseDTO toDto(BookInstance entity) {
        Integer donorId = entity.getUserDonor() != null ? entity.getUserDonor().getId() : null;
        String donorName = entity.getUserDonor() != null ? entity.getUserDonor().getName() : null;

        return new BookInstanceResponseDTO(
                entity.getId(),
                entity.getBook().getId(),
                entity.getBook().getTitle(),
                entity.getBook().getAuthor(),
                entity.getBook().getIsbn(),
                entity.getBook().getThumbnailUrl(),
                entity.getBook().getCategory(),
                entity.getStatus(),
                entity.getCollectionPoint().getId(),
                entity.getCollectionPoint().getName(),
                donorId,
                donorName,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}