package com.backend.circuler.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", description = "0: APAGADO, 1: ATIVO, 3: PENDENTE")
public enum BookStatus {
    APAGADO(0, "APAGADO"),
    ATIVO(1, "ATIVO"),
    PENDENTE(3, "PENDENTE");

    private final Integer code;
    private final String description;

    BookStatus(Integer code, String description) {
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
    public static BookStatus fromCode(Integer code) {
        if (code == null) return null;
        for (BookStatus status : BookStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status inválido: " + code);
    }
}
