package com.backend.circuler.mapper;

import com.backend.circuler.dto.reservation.ReservationResponseDTO;
import com.backend.circuler.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toDto(Reservation entity) {
        return new ReservationResponseDTO(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getBookInstance().getId(),
                entity.getBookInstance().getBook().getTitle(),
                entity.getBookInstance().getBook().getAuthor(),
                entity.getBookInstance().getCollectionPoint().getId(),
                entity.getBookInstance().getCollectionPoint().getName(),
                entity.getBookInstance().getCollectionPoint().getAddressStreet(),
                entity.getBookInstance().getCollectionPoint().getAddressNeighborhood(),
                entity.getBookInstance().getBook().getThumbnailUrl(),
                entity.getVerificationCode(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
