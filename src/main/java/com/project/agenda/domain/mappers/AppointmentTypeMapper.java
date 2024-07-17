package com.project.agenda.domain.mappers;

import com.project.agenda.domain.entities.AppointmentType;
import com.project.agenda.dto.AppointmentTypeResponse;

public class AppointmentTypeMapper {
    
    public static AppointmentTypeResponse toAppointmentTypeResponseDTO(AppointmentType appointmentType) {

        return new AppointmentTypeResponse(appointmentType.getId(), appointmentType.getType());
    }
}
