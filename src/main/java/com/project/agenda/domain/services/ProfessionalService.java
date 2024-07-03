package com.project.agenda.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.mappers.TimeSlotMapper;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
import com.project.agenda.dto.TimeSlotResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessionalService {
    
    @Autowired
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;

    @Autowired
    private ProfessionalRepository professionalRepository;

    public List<TimeSlotResponse> getAvailabilityTimes(Long professionalId, LocalDate date) {

        Professional professional = professionalRepository
                        .findById(professionalId)
                        .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        List<TimeSlot> timeSlots = this.searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional, date);
        
        return timeSlots.stream().map(ts -> TimeSlotMapper.toTimeSlotResponseDTO(ts)).collect(Collectors.toList());
    }
}
