package com.project.agenda.dto;

import java.time.LocalTime;

public record TimeSlotResponse(LocalTime startTime, LocalTime endTime, boolean available) {}