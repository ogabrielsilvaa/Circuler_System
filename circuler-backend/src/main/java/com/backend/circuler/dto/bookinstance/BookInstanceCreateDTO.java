package com.backend.circuler.dto.bookinstance;

public class BookInstanceCreateDTO {

    private Integer bookId;
    private Integer userDonorId;

    public BookInstanceCreateDTO() {}

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public Integer getUserDonorId() { return userDonorId; }
    public void setUserDonorId(Integer userDonorId) { this.userDonorId = userDonorId; }
}