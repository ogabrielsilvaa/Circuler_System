package com.backend.circuler.enums.converter;

import com.backend.circuler.enums.BookStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BookStatus status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public BookStatus convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return BookStatus.fromCode(code);
    }
}
