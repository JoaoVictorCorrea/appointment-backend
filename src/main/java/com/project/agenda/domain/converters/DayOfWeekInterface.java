package com.project.agenda.domain.converters;

import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;

public interface DayOfWeekInterface extends AttributeConverter<DayOfWeek, Integer> {

}
