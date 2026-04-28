package com.backend.circuler.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", description = "0: APAGADO, 1: ATIVO, 2: LOTADO, 3: INATIVO")
public enum CollectionPointStatus {
    APAGADO(0, "APAGADO"),
    ATIVO(1, "ATIVO"),
    LOTADO(2, "LOTADO"),
    INATIVO(3, "INATIVO");

    private final Integer code;
    private final String description;

    CollectionPointStatus(Integer code, String description) {
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
    public static CollectionPointStatus fromCode(Integer code) {
        if (code == null) return null;
        for (CollectionPointStatus status : CollectionPointStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status inválido: " + code);
    }
}
