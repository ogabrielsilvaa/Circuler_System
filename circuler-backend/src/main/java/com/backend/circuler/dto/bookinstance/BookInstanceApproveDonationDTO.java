package com.backend.circuler.dto.bookinstance;

public class BookInstanceApproveDonationDTO {

    private String bookThumbnailUrl;
    private String bookIsbn;

    public BookInstanceApproveDonationDTO() {}

    public String getBookThumbnailUrl() { return bookThumbnailUrl; }
    public void setBookThumbnailUrl(String bookThumbnailUrl) { this.bookThumbnailUrl = bookThumbnailUrl; }

    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
}
