package com.project.agenda.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.mappers.TimeSlotMapper;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import com.project.agenda.domain.services.exceptions.ParameterException;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityDaysUseCase;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
import com.project.agenda.dto.TimeSlotResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessionalService {
    
    @Autowired
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;

    @Autowired
    private SearchProfessionalAvailabilityDaysUseCase searchProfessionalAvailabilityDaysUseCase;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Transactional(readOnly = true)
    public List<TimeSlotResponse> getAvailabilityTimesFromProfessional(Long professionalId, LocalDate date) {

        Professional professional = getProfessional(professionalId);

        List<TimeSlot> timeSlots = this.searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional, date);

        return timeSlots.stream().map(ts -> TimeSlotMapper.toTimeSlotResponseDTO(ts)).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Integer> getAvailabilityDaysFromProfessional(long professionalId, int month, int year) {

        checkProfessionalExistsOrThrowsException(professionalId);
        checkMonthIsValidOrThrowsException(month);
        checkYearIsValidOrThrowsException(year);
        checkMonthAndCurrentYearAreValidOrThrowsException(month, year);

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return this.searchProfessionalAvailabilityDaysUseCase.executeUseCase(professionalId, start, end);
    }

    private void checkProfessionalExistsOrThrowsException(long professionalId) {

        if(!professionalRepository.existsById(professionalId))
            throw new EntityNotFoundException("Profissional não encontrado.");
    }

    private Professional getProfessional(long professionalId) {
        return professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));
    }

    private void checkMonthAndCurrentYearAreValidOrThrowsException(int month, int year) {
        if(year == LocalDate.now().getYear() && month < LocalDate.now().getMonthValue())
            throw new ParameterException("Mês inválido. O mês deve ser maior ou igual ao mês corrente.");
    }

    private void checkYearIsValidOrThrowsException(int year) {
        if(year < LocalDate.now().getYear())
            throw new ParameterException("Ano inválido. O ano deve ser maior ou igual ao ano corrente.");
    }

    private void checkMonthIsValidOrThrowsException(int month) {
        if(month < 1 || month > 12)
            throw new ParameterException("Mês inválido. Utilize um valor de 1 à 12.");
    }
}
