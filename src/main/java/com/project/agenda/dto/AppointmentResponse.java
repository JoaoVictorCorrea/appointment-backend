package com.project.agenda.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.project.agenda.domain.entities.AppointmentStatus;

public record AppointmentResponse(
        long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String comments,
        AppointmentStatus status,
        IntegerDTO type,
        IntegerDTO area,
        LongDTO professional,
        LongDTO client
) {}
