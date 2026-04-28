package com.backend.circuler.dto.book;

import com.backend.circuler.enums.BookCategory;
import com.backend.circuler.enums.BookStatus;

import java.time.LocalDateTime;

public class BookResponseDTO {

    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String thumbnailUrl;
    private BookCategory category;
    private String isbn;
    private BookStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookResponseDTO() {}

    public BookResponseDTO(Integer id, String title, String author, String publisher,
                           String thumbnailUrl, BookCategory category, String isbn,
                           BookStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.isbn = isbn;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public BookCategory getCategory() { return category; }
    public void setCategory(BookCategory category) { this.category = category; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
