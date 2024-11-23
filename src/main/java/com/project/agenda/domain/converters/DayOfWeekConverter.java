package com.project.agenda.domain.converters;

import java.time.DayOfWeek;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Autowired
    private DayOfWeekInterface converter;

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        return converter.convertToDatabaseColumn(dayOfWeek);
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer dayOfWeek) {
        return converter.convertToEntityAttribute(dayOfWeek);
    }
}
