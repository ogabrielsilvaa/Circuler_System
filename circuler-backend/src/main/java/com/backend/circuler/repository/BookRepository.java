package com.backend.circuler.repository;

import com.backend.circuler.entity.Book;
import com.backend.circuler.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByIdAndStatusNot(Integer id, BookStatus status);

    List<Book> findAllByStatusNot(BookStatus status);

    List<Book> findAllByStatus(BookStatus status);

    @Modifying
    @Query("UPDATE Book b SET b.status = :status WHERE b.id = :id")
    void logicalDeleteById(@Param("id") Integer id, @Param("status") BookStatus status);
}
