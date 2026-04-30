package com.backend.circuler.dto.bookinstance;

import com.backend.circuler.enums.BookInstanceStatus;

public class BookInstanceUpdateDTO {

    private BookInstanceStatus status;

    public BookInstanceUpdateDTO() {}

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }
}