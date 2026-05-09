package com.backend.circuler.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", description = "0: APAGADA, 1: ATIVA, 2: CONCLUIDA, 3: CANCELADA")
public enum ReservationStatus {
    APAGADA(0, "APAGADA"),
    ATIVA(1, "ATIVA"),
    CONCLUIDA(2, "CONCLUIDA"),
    CANCELADA(3, "CANCELADA");

    private final Integer code;
    private final String description;

    ReservationStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static ReservationStatus fromCode(Integer code) {
        if (code == null) return null;
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de reserva inválido: " + code);
    }
}
