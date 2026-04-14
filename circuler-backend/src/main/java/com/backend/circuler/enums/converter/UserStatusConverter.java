package com.backend.circuler.enums.converter;

import com.backend.circuler.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatus status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return UserStatus.fromCode(code);
    }
}
