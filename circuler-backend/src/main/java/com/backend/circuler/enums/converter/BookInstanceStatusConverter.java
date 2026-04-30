package com.backend.circuler.enums.converter;

import com.backend.circuler.enums.BookInstanceStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookInstanceStatusConverter implements AttributeConverter<BookInstanceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BookInstanceStatus status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public BookInstanceStatus convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return BookInstanceStatus.fromCode(code);
    }
}