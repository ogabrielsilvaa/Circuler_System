package com.backend.circuler.enums.converter;

import com.backend.circuler.enums.ReservationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReservationStatus status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return ReservationStatus.fromCode(code);
    }
}
