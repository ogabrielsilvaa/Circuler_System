package com.backend.circuler.mapper;

import com.backend.circuler.dto.collectionpoint.CollectionPointCreateDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointResponseDTO;
import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.CollectionPointStatus;
import org.springframework.stereotype.Component;

@Component
public class CollectionPointMapper {

    public CollectionPoint toEntity(CollectionPointCreateDTO createDTO, User userAdmin) {
        CollectionPoint point = new CollectionPoint();
        point.setName(createDTO.getName());
        point.setAddressStreet(createDTO.getAddressStreet());
        point.setAddressNeighborhood(createDTO.getAddressNeighborhood());
        point.setCapacityLimit(createDTO.getCapacityLimit());
        point.setUserAdmin(userAdmin);
        point.setStatus(CollectionPointStatus.ATIVO);
        return point;
    }

    public CollectionPointResponseDTO toDto(CollectionPoint entity) {
        return new CollectionPointResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getAddressStreet(),
                entity.getAddressNeighborhood(),
                entity.getCapacityLimit(),
                entity.getStatus(),
                entity.getUserAdmin().getId(),
                entity.getUserAdmin().getName(),
                entity.getUserAdmin().getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
