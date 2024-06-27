package com.project.agenda.domain.mappers;

import org.springframework.beans.BeanUtils;

import com.project.agenda.domain.entities.Appointment;
import com.project.agenda.domain.entities.AppointmentType;
import com.project.agenda.domain.entities.Area;
import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.dto.AppointmentRequest;
import com.project.agenda.dto.AppointmentResponse;
import com.project.agenda.dto.IntegerDTO;
import com.project.agenda.dto.LongDTO;

public class AppointmentMapper {

    public static AppointmentResponse toAppointmentResponseDTO(Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getComments(),
                appointment.getStatus(),
                new IntegerDTO(appointment.getAppointmentType().getId()),
                new IntegerDTO(appointment.getArea().getId()),
                new LongDTO(appointment.getProfessional().getId()),
                new LongDTO(appointment.getClient().getId()));
    }
    
    public static Appointment fromAppointmentRequestDTO(AppointmentRequest appointmentRequest) {
        
        Appointment appointment = new Appointment();

        BeanUtils.copyProperties(appointmentRequest, appointment);
        appointment.setArea(new Area(appointmentRequest.area().id()));
        appointment.setAppointmentType(new AppointmentType(appointmentRequest.type().id()));
        appointment.setProfessional(new Professional(appointmentRequest.professional().id()));
        appointment.setClient(new Client(appointmentRequest.client().id()));

        return appointment;
    }
}
