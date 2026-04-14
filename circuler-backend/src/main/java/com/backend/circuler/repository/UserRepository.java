package com.backend.circuler.repository;

import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndStatusNot(Integer id, UserStatus status);

    List<User> findAllByStatusNot(UserStatus status);

    List<User> findAll();

    @Modifying
    @Query("UPDATE User u SET u.status = :statusInativo WHERE u.id = :userId")
    void logicalDeleteById(@Param("id") Integer id);
}
