package com.backend.circuler.repository;

import com.backend.circuler.entity.BookInstance;
import com.backend.circuler.enums.BookInstanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookInstanceRepository extends JpaRepository<BookInstance, Integer> {

    Optional<BookInstance> findByIdAndStatusNot(Integer id, BookInstanceStatus status);

    List<BookInstance> findAllByStatusNot(BookInstanceStatus status);

    List<BookInstance> findAllByCollectionPointIdAndStatusNot(Integer collectionPointId, BookInstanceStatus status);

    long countByCollectionPointIdAndStatusNot(Integer collectionPointId, BookInstanceStatus status);

    @Modifying
    @Query("UPDATE BookInstance bi SET bi.status = :status WHERE bi.id = :id")
    void logicalDeleteById(@Param("id") Integer id, @Param("status") BookInstanceStatus status);
}