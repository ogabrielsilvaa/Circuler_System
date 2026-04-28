package com.backend.circuler.enums.converter;

import com.backend.circuler.enums.BookCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookCategoryConverter implements AttributeConverter<BookCategory, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BookCategory category) {
        if (category == null) return null;
        return category.getCode();
    }

    @Override
    public BookCategory convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return BookCategory.fromCode(code);
    }
}
