package com.backend.circuler.dto.collectionpoint;

import com.backend.circuler.enums.BookCategory;
import com.backend.circuler.enums.BookInstanceStatus;

public class CollectionPointBookDTO {

    private Integer id;
    private Integer bookId;
    private String bookTitle;
    private String bookAuthor;
    private BookCategory bookCategory;
    private BookInstanceStatus status;
    private Integer userDonorId;
    private String userDonorName;

    public CollectionPointBookDTO() {}

    public CollectionPointBookDTO(Integer id, Integer bookId, String bookTitle, String bookAuthor,
                                  BookCategory bookCategory, BookInstanceStatus status,
                                  Integer userDonorId, String userDonorName) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
        this.status = status;
        this.userDonorId = userDonorId;
        this.userDonorName = userDonorName;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public BookCategory getBookCategory() { return bookCategory; }
    public void setBookCategory(BookCategory bookCategory) { this.bookCategory = bookCategory; }

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }

    public Integer getUserDonorId() { return userDonorId; }
    public void setUserDonorId(Integer userDonorId) { this.userDonorId = userDonorId; }

    public String getUserDonorName() { return userDonorName; }
    public void setUserDonorName(String userDonorName) { this.userDonorName = userDonorName; }
}
