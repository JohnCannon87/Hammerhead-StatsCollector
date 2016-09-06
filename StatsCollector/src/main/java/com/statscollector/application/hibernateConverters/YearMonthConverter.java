package com.statscollector.application.hibernateConverters;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, Date> {

    @Override
    public Date convertToDatabaseColumn(final YearMonth attribute) {
        return Date
                .from(attribute.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public YearMonth convertToEntityAttribute(final Date dbData) {
        return YearMonth.from(dbData.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
    }
}
