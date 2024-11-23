package com.project.agenda.domain.converters;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Profile({"test", "runner"})
@Component
public class DayOfWeekConverterH2 implements DayOfWeekInterface{

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:        return 2;
            case TUESDAY:       return 3;
            case WEDNESDAY:     return 4;
            case THURSDAY:      return 5;
            case FRIDAY:        return 6;
            case SATURDAY:      return 7;
            case SUNDAY:        return 1;
            default:
                throw new IllegalArgumentException("Dia da semana inválido.");
        }
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer dayOfWeek) {
        switch (dayOfWeek) {
            case 2:     return DayOfWeek.MONDAY;
            case 3:     return DayOfWeek.TUESDAY;
            case 4:     return DayOfWeek.WEDNESDAY;
            case 5:     return DayOfWeek.THURSDAY;
            case 6:     return DayOfWeek.FRIDAY;
            case 7:     return DayOfWeek.SATURDAY;
            case 1:     return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Dia da semana inválido.");
        }
    }
}
