package com.project.agenda.domain.services.usecases.read;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.agenda.domain.repositories.AppointmentRepository;

@Service
public class SearchProfessionalAvailabilityDaysUseCase {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public List<Integer> executeUseCase(long professionalId, LocalDate start, LocalDate end) {

        return this.appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
    }
}
