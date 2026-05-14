package com.backend.circuler.dto.bookinstance;

import com.backend.circuler.enums.BookCategory;
import com.backend.circuler.enums.BookInstanceStatus;

import java.time.LocalDateTime;

public class BookInstanceResponseDTO {

    private Integer id;
    private Integer bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    private String bookThumbnailUrl;
    private BookCategory bookCategory;
    private BookInstanceStatus status;
    private Integer collectionPointId;
    private String collectionPointName;
    private String collectionPointAddressStreet;
    private String collectionPointAddressNeighborhood;
    private String collectionPointOwnerName;
    private Integer userDonorId;
    private String userDonorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookInstanceResponseDTO() {}

    public BookInstanceResponseDTO(Integer id, Integer bookId, String bookTitle, String bookAuthor,
                                   String bookIsbn, String bookThumbnailUrl,
                                   BookCategory bookCategory, BookInstanceStatus status,
                                   Integer collectionPointId, String collectionPointName,
                                   String collectionPointAddressStreet,
                                   String collectionPointAddressNeighborhood,
                                   String collectionPointOwnerName,
                                   Integer userDonorId, String userDonorName,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookIsbn = bookIsbn;
        this.bookThumbnailUrl = bookThumbnailUrl;
        this.bookCategory = bookCategory;
        this.status = status;
        this.collectionPointId = collectionPointId;
        this.collectionPointName = collectionPointName;
        this.collectionPointAddressStreet = collectionPointAddressStreet;
        this.collectionPointAddressNeighborhood = collectionPointAddressNeighborhood;
        this.collectionPointOwnerName = collectionPointOwnerName;
        this.userDonorId = userDonorId;
        this.userDonorName = userDonorName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }

    public String getBookThumbnailUrl() { return bookThumbnailUrl; }
    public void setBookThumbnailUrl(String bookThumbnailUrl) { this.bookThumbnailUrl = bookThumbnailUrl; }

    public BookCategory getBookCategory() { return bookCategory; }
    public void setBookCategory(BookCategory bookCategory) { this.bookCategory = bookCategory; }

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }

    public Integer getCollectionPointId() { return collectionPointId; }
    public void setCollectionPointId(Integer collectionPointId) { this.collectionPointId = collectionPointId; }

    public String getCollectionPointName() { return collectionPointName; }
    public void setCollectionPointName(String collectionPointName) { this.collectionPointName = collectionPointName; }

    public String getCollectionPointAddressStreet() { return collectionPointAddressStreet; }
    public void setCollectionPointAddressStreet(String collectionPointAddressStreet) { this.collectionPointAddressStreet = collectionPointAddressStreet; }

    public String getCollectionPointAddressNeighborhood() { return collectionPointAddressNeighborhood; }
    public void setCollectionPointAddressNeighborhood(String collectionPointAddressNeighborhood) { this.collectionPointAddressNeighborhood = collectionPointAddressNeighborhood; }

    public String getCollectionPointOwnerName() { return collectionPointOwnerName; }
    public void setCollectionPointOwnerName(String collectionPointOwnerName) { this.collectionPointOwnerName = collectionPointOwnerName; }

    public Integer getUserDonorId() { return userDonorId; }
    public void setUserDonorId(Integer userDonorId) { this.userDonorId = userDonorId; }

    public String getUserDonorName() { return userDonorName; }
    public void setUserDonorName(String userDonorName) { this.userDonorName = userDonorName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}