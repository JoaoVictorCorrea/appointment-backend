package com.project.agenda.domain.converters;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Profile({"dev", "prod"})
@Component
public class DayOfWeekConverterPostgre implements DayOfWeekInterface{

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:        return 1;
            case TUESDAY:       return 2;
            case WEDNESDAY:     return 3;
            case THURSDAY:      return 4;
            case FRIDAY:        return 5;
            case SATURDAY:      return 6;
            case SUNDAY:        return 0;
            default:
                throw new IllegalArgumentException("Dia da semana inválido.");
        }
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer dayOfWeek) {
        switch (dayOfWeek) {
            case 1:     return DayOfWeek.MONDAY;
            case 2:     return DayOfWeek.TUESDAY;
            case 3:     return DayOfWeek.WEDNESDAY;
            case 4:     return DayOfWeek.THURSDAY;
            case 5:     return DayOfWeek.FRIDAY;
            case 6:     return DayOfWeek.SATURDAY;
            case 0:     return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Dia da semana inválido.");
        }
    }
}
