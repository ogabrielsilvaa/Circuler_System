package com.backend.circuler.dto.reservation;

import jakarta.validation.constraints.NotNull;

public class ReservationCreateDTO {

    @NotNull(message = "O id do exemplar é obrigatório")
    private Integer bookInstanceId;

    public ReservationCreateDTO() {}

    public Integer getBookInstanceId() { return bookInstanceId; }
    public void setBookInstanceId(Integer bookInstanceId) { this.bookInstanceId = bookInstanceId; }
}
