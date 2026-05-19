package com.backend.circuler.repository;

import com.backend.circuler.entity.Reservation;
import com.backend.circuler.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Optional<Reservation> findByIdAndStatusNot(Integer id, ReservationStatus status);

    List<Reservation> findAllByStatusNot(ReservationStatus status);

    List<Reservation> findAllByUserIdAndStatusNot(Integer userId, ReservationStatus status);

    List<Reservation> findAllByBookInstanceCollectionPointIdAndStatusNot(Integer collectionPointId, ReservationStatus status);

    Optional<Reservation> findByBookInstanceIdAndStatus(Integer bookInstanceId, ReservationStatus status);

    boolean existsByBookInstanceIdAndStatus(Integer bookInstanceId, ReservationStatus status);

    @Modifying
    @Query("UPDATE Reservation r SET r.status = :status WHERE r.id = :id")
    void logicalDeleteById(@Param("id") Integer id, @Param("status") ReservationStatus status);
}
