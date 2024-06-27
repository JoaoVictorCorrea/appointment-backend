package com.project.agenda.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String comments,
        IntegerDTO type,
        IntegerDTO area,
        LongDTO professional,
        LongDTO client    
) {}
