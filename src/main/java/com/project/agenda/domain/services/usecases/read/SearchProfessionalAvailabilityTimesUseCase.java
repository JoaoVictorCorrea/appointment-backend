package com.project.agenda.domain.services.usecases.read;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.models.TimeSlot;

@Service
public class SearchProfessionalAvailabilityTimesUseCase {
    
    public List<TimeSlot> executeUseCase(Professional professional, LocalDate date) {

        //TODO - Create a real execute case
        
        TimeSlot t1 = new TimeSlot(LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), true);
        TimeSlot t2 = new TimeSlot(LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), true);
        TimeSlot t3 = new TimeSlot(LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"), false);
        TimeSlot t4 = new TimeSlot(LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"), true);
        TimeSlot t5 = new TimeSlot(LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"), true);

        return Arrays.asList(t1, t2, t3, t4, t5);
    }
}
