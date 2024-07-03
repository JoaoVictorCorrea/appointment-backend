package com.project.agenda.domain.mappers;

import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.dto.TimeSlotResponse;

public class TimeSlotMapper {
    
    public static TimeSlotResponse toTimeSlotResponseDTO(TimeSlot timeSlot) {

        return new TimeSlotResponse(timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.isAvailable());
    }
}
