package com.backend.circuler.enums.converter;

import com.backend.circuler.enums.CollectionPointStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CollectionPointStatusConverter implements AttributeConverter<CollectionPointStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CollectionPointStatus status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public CollectionPointStatus convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return CollectionPointStatus.fromCode(code);
    }
}
