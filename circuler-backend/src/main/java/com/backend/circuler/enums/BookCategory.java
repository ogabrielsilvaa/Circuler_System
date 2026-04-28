package com.backend.circuler.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", description = "1: INFANTIL_JUVENIL, 2: AUTOAJUDA, 3: DIDATICO, 4: ESCOLAR")
public enum BookCategory {
    INFANTIL_JUVENIL(1, "INFANTIL_JUVENIL"),
    AUTOAJUDA(2, "AUTOAJUDA"),
    DIDATICO(3, "DIDATICO"),
    ESCOLAR(4, "ESCOLAR");

    private final Integer code;
    private final String description;

    BookCategory(Integer code, String description) {
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
    public static BookCategory fromCode(Integer code) {
        if (code == null) return null;
        for (BookCategory category : BookCategory.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Código de categoria inválido: " + code);
    }
}
