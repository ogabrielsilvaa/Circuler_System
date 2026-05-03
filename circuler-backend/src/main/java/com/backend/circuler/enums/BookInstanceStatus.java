package com.backend.circuler.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", description = "0: APAGADO, 1: DISPONIVEL, 2: RESERVADO, 3: RETIRADO, 4: PENDENTE")
public enum BookInstanceStatus {
    APAGADO(0, "APAGADO"),
    DISPONIVEL(1, "DISPONIVEL"),
    RESERVADO(2, "RESERVADO"),
    RETIRADO(3, "RETIRADO"),
    PENDENTE(4, "PENDENTE");

    private final Integer code;
    private final String description;

    BookInstanceStatus(Integer code, String description) {
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
    public static BookInstanceStatus fromCode(Integer code) {
        if (code == null) return null;
        for (BookInstanceStatus status : BookInstanceStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de exemplar inválido: " + code);
    }
}