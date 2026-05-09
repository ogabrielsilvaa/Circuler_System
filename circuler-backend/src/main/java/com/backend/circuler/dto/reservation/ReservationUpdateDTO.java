package com.backend.circuler.dto.reservation;

import com.backend.circuler.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class ReservationUpdateDTO {

    @Schema(
            type = "integer",
            allowableValues = {"2", "3"},
            description = "Novo status da reserva: 2 - CONCLUIDA, 3 - CANCELADA",
            example = "2"
    )
    private ReservationStatus status;

    public ReservationUpdateDTO() {}

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
