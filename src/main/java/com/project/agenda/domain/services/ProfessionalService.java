package com.project.agenda.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.agenda.domain.entities.Area;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.mappers.ProfessionalMapper;
import com.project.agenda.domain.mappers.TimeSlotMapper;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.AreaRepository;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import com.project.agenda.domain.services.exceptions.BusinessException;
import com.project.agenda.domain.services.exceptions.DatabaseException;
import com.project.agenda.domain.services.exceptions.ParameterException;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityDaysUseCase;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
import com.project.agenda.dto.ProfessionalRequest;
import com.project.agenda.dto.ProfessionalResponse;
import com.project.agenda.dto.ProfessionalWithAreaResponse;
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

    @Autowired
    private AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public Page<ProfessionalResponse> findByNameContainingIgnoreCase(String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Professional> pageProfessional = professionalRepository.findByNameContainingIgnoreCase(name, pageRequest);

        return pageProfessional.map(p -> ProfessionalMapper.toProfessionalResponseDTO(p));
    }

    @Transactional(readOnly = true)
    public ProfessionalResponse getById(long id) {

        Professional professional = professionalRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        return ProfessionalMapper.toProfessionalResponseDTO(professional);
    }

    @Transactional
    public ProfessionalResponse save(ProfessionalRequest professionalRequest) {

        Professional professional = professionalRepository
                .save(ProfessionalMapper.fromProfessionalRequestDTO(professionalRequest));

        return ProfessionalMapper.toProfessionalResponseDTO(professional);
    }
    
    @Transactional
    public void update(long id, ProfessionalRequest professionalRequest) {

        try {
            Professional professional = professionalRepository.getReferenceById(id);

            professional.setName(professionalRequest.name());
            professional.setPhone(professionalRequest.phone());
            professional.setActive(professionalRequest.active());

            professionalRepository.save(professional);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Profissional não encontrado.");
        }
    }
    
    @Transactional
    public void deleteById(long id) {

        try {
            if (!professionalRepository.existsById(id))
                throw new EntityNotFoundException("Profissional não encontrado.");

            professionalRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Conflito ao remover o profissional.");
        }
    }
    
    @Transactional
    public ProfessionalWithAreaResponse associateProfessionalWithArea(long id_professional, int id_area) {

        Area area = areaRepository
                .findById(id_area)
                .orElseThrow(() -> new EntityNotFoundException("Area não encontrada."));

        Professional professional = professionalRepository
                .findById(id_professional)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        if (isProfessionalAssociatedWithArea(professional, area))
            throw new BusinessException("O profissional já trabalha esta area.");

        professional.getAreas().add(area);
        Professional professionalWithNewArea = professionalRepository.save(professional);

        return ProfessionalMapper.toProfessionalWithAreaResponseDTO(professionalWithNewArea);
    }
    
    @Transactional
    public void disassociateProfessionalWithArea(long id_professional, int id_area) {
        
        Area area = areaRepository
                .findById(id_area)
                .orElseThrow(() -> new EntityNotFoundException("Area não encontrada."));

        Professional professional = professionalRepository
                .findById(id_professional)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        if (!isProfessionalAssociatedWithArea(professional, area))
            throw new BusinessException("O profissional não trabalha nesta area.");

        professional.getAreas().remove(area);
        professionalRepository.save(professional);
    }

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
        if (month < 1 || month > 12)
            throw new ParameterException("Mês inválido. Utilize um valor de 1 à 12.");
    }
    
    private boolean isProfessionalAssociatedWithArea(Professional professional, Area area) { 
        return professional.getAreas().contains(area);
    }
}
