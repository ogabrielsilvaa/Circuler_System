package com.backend.circuler.repository;

import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.enums.CollectionPointStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Integer> {

    Optional<CollectionPoint> findByIdAndStatusNot(Integer id, CollectionPointStatus status);

    List<CollectionPoint> findAllByStatusNot(CollectionPointStatus status);

    boolean existsByUserAdminIdAndStatusNot(Integer userAdminId, CollectionPointStatus status);

    boolean existsByUserAdminIdAndStatusNotAndIdNot(Integer userAdminId, CollectionPointStatus status, Integer id);

    @Modifying
    @Query("UPDATE CollectionPoint cp SET cp.status = :status WHERE cp.id = :id")
    void logicalDeleteById(@Param("id") Integer id, @Param("status") CollectionPointStatus status);
}
