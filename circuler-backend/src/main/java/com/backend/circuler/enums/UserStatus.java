package com.backend.circuler.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", description = "1: ATIVO, 2: INATIVO, 0: APAGADO")
public enum UserStatus {
    APAGADO(0, "APAGADO"),
    ATIVO(1, "ATIVO"),
    INATIVO(2, "INATIVO");

    private final Integer code;
    private final String description;

    UserStatus(Integer code, String description) {
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
    public static UserStatus fromCode(Integer code) {
        if (code == null) return null;
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status inválido: " + code);
    }
}
