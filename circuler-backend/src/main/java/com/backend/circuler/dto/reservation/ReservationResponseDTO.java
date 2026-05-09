package com.backend.circuler.dto.reservation;

import com.backend.circuler.enums.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationResponseDTO {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer bookInstanceId;
    private String bookTitle;
    private String bookAuthor;
    private Integer collectionPointId;
    private String collectionPointName;
    private String verificationCode;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReservationResponseDTO() {}

    public ReservationResponseDTO(Integer id, Integer userId, String userName,
                                   Integer bookInstanceId, String bookTitle, String bookAuthor,
                                   Integer collectionPointId, String collectionPointName,
                                   String verificationCode, ReservationStatus status,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.bookInstanceId = bookInstanceId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.collectionPointId = collectionPointId;
        this.collectionPointName = collectionPointName;
        this.verificationCode = verificationCode;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getBookInstanceId() { return bookInstanceId; }
    public void setBookInstanceId(Integer bookInstanceId) { this.bookInstanceId = bookInstanceId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public Integer getCollectionPointId() { return collectionPointId; }
    public void setCollectionPointId(Integer collectionPointId) { this.collectionPointId = collectionPointId; }

    public String getCollectionPointName() { return collectionPointName; }
    public void setCollectionPointName(String collectionPointName) { this.collectionPointName = collectionPointName; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
