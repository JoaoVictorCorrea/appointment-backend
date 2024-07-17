package com.project.agenda.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.agenda.domain.entities.Appointment;
import com.project.agenda.domain.mappers.AppointmentMapper;
import com.project.agenda.domain.services.usecases.write.CreateAppointmentUseCase;
import com.project.agenda.dto.AppointmentRequest;
import com.project.agenda.dto.AppointmentResponse;

@Service
public class AppointmentService {

    @Autowired
    private CreateAppointmentUseCase createAppointmentUseCase;
    
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {

        Appointment appointment = createAppointmentUseCase.executeUseCase(AppointmentMapper.fromAppointmentRequestDTO(appointmentRequest));
        
        return AppointmentMapper.toAppointmentResponseDTO(appointment);
    }
}
