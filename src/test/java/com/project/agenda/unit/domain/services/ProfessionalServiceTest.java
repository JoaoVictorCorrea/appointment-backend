package com.project.agenda.unit.domain.services;

import com.project.agenda.domain.entities.Area;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.AreaRepository;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import com.project.agenda.domain.services.ProfessionalService;
import com.project.agenda.domain.services.exceptions.BusinessException;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityDaysUseCase;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
import com.project.agenda.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessionalServiceTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;

    @Mock
    private SearchProfessionalAvailabilityDaysUseCase searchProfessionalAvailabilityDaysUseCase;

    @InjectMocks
    private ProfessionalService professionalService;

    @Test
    void saveShouldPersistProfessional(){

        Professional newProfessional = new Professional("Joice", "119999999", true);
        Professional savedProfessional = new Professional(1L, "Joice", "119999999", true);

        given(professionalRepository.save(newProfessional)).willReturn(savedProfessional);

        ProfessionalRequest newProfessionalRequest = new ProfessionalRequest("Joice", "119999999", true);
        ProfessionalResponse expProfessionalResponse = new ProfessionalResponse(1L, "Joice", "119999999", true, new IntegerDTO(1));

        ProfessionalResponse foundProfessionalResponse = professionalService.save(newProfessionalRequest);

        verify(professionalRepository).save(any(Professional.class));

        assertNotNull(foundProfessionalResponse);
        assertNotNull(foundProfessionalResponse.id());

        assertEquals(expProfessionalResponse.id(), foundProfessionalResponse.id());
        assertEquals(expProfessionalResponse.name(), foundProfessionalResponse.name());
        assertEquals(expProfessionalResponse.phone(), foundProfessionalResponse.phone());
        assertEquals(expProfessionalResponse.active(), foundProfessionalResponse.active());
    }

    @Test
    void associateProfessionalWithAreaShouldPersistProfessional(){

        Area area = new Area(1, "Dermatologista");
        given(areaRepository.findById(area.getId())).willReturn(Optional.of(area));

        Professional professional = new Professional(1L,"Joice", "119999999", true);
        given(professionalRepository.findById(professional.getId())).willReturn(Optional.of(professional));

        Set<Area> areas = new HashSet<>();
        areas.add(area);
        Professional savedProfessional = new Professional(1L, "Joice", "119999999", true, areas);

        given(professionalRepository.save(professional)).willReturn(savedProfessional);

        Set<AreaResponse> areasResponse = new HashSet<>();
        AreaResponse areaResponse = new AreaResponse(1, "Dermatologista");
        areasResponse.add(areaResponse);
        ProfessionalWithAreaResponse expProfessionalWithAreaResponse = new ProfessionalWithAreaResponse(1L, "Joice", "119999999", true, areasResponse);

        ProfessionalWithAreaResponse foundProfessionalWithAreaResponse = professionalService.associateProfessionalWithArea(professional.getId(), area.getId());

        verify(areaRepository).findById(anyInt());
        verify(professionalRepository).findById(anyLong());
        verify(professionalRepository).save(any(Professional.class));

        assertNotNull(foundProfessionalWithAreaResponse);
        assertNotNull(foundProfessionalWithAreaResponse.id());

        assertEquals(expProfessionalWithAreaResponse.id(), foundProfessionalWithAreaResponse.id());
        assertEquals(expProfessionalWithAreaResponse.name(), foundProfessionalWithAreaResponse.name());
        assertEquals(expProfessionalWithAreaResponse.phone(), foundProfessionalWithAreaResponse.phone());
        assertEquals(expProfessionalWithAreaResponse.active(), foundProfessionalWithAreaResponse.active());
        assertIterableEquals(expProfessionalWithAreaResponse.areas(), foundProfessionalWithAreaResponse.areas());
    }

    @Test
    void associateProfessionalWithAreaShouldThrowsBusinessException(){

        Area area = new Area(1, "Dermatologista");
        given(areaRepository.findById(area.getId())).willReturn(Optional.of(area));

        Professional professional = new Professional(1L,"Joice", "119999999", true);
        professional.getAreas().add(area);
        given(professionalRepository.findById(professional.getId())).willReturn(Optional.of(professional));

        assertThrows(BusinessException.class, () -> professionalService.associateProfessionalWithArea(professional.getId(), area.getId()));
        verify(areaRepository).findById(anyInt());
        verify(professionalRepository).findById(anyLong());
    }

    @Test
    void disassociateProfessionalWithAreaShouldPersistProfessional(){

        Area area1 = new Area(1, "Dermatologista");
        Area area2 = new Area(2, "Cardiologista");
        Area area3 = new Area(3, "Pediatra");
        given(areaRepository.findById(area1.getId())).willReturn(Optional.of(area1));

        Professional professional = new Professional(1L,"Joice", "119999999", true);
        given(professionalRepository.findById(professional.getId())).willReturn(Optional.of(professional));
        given(professionalRepository.save(any(Professional.class))).willReturn(any(Professional.class));

        Set<Area> areas = new HashSet<>();
        areas.add(area1);
        areas.add(area2);
        areas.add(area3);
        professional.setAreas(areas);

        professionalService.disassociateProfessionalWithArea(professional.getId(), area1.getId());

        verify(areaRepository).findById(anyInt());
        verify(professionalRepository).findById(anyLong());
        verify(professionalRepository).save(any(Professional.class));
        assertFalse(professional.getAreas().contains(area1));
    }

    @Test
    void disassociateProfessionalWithAreaShouldThrowBusinessException() {

        Area area = new Area(1, "Dermatologista");
        given(areaRepository.findById(area.getId())).willReturn(Optional.of(area));

        Professional professional = new Professional(1L, "Joice", "119999999", true);
        given(professionalRepository.findById(professional.getId())).willReturn(Optional.of(professional));

        //professional is not associated with area
        assertThrows(BusinessException.class, () -> professionalService.disassociateProfessionalWithArea(professional.getId(), area.getId()));

        verify(areaRepository).findById(anyInt());
        verify(professionalRepository).findById(anyLong());
        verify(professionalRepository, never()).save(any(Professional.class));
    }

    @Test
    void getAvailabilityTimesFromProfessionalShouldReturnAvailabilityTimesFromProfessional() {

        Long professionalId = 1L;
        LocalDate date = LocalDate.of(2024, 3, 25);
        Professional professional = new Professional(professionalId, "Joice", "119999999", true);

        List<TimeSlot> timeSlots = createTimeSlots(true);

        given(professionalRepository.findById(professionalId)).willReturn(Optional.of(professional));
        given(searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional, date)).willReturn(timeSlots);

        List<TimeSlotResponse> responses = professionalService.getAvailabilityTimesFromProfessional(professionalId, date);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(LocalTime.of(8, 0), responses.get(0).startTime());
        assertEquals(LocalTime.of(8, 30), responses.get(0).endTime());
        assertEquals(LocalTime.of(8, 30), responses.get(1).startTime());
        assertEquals(LocalTime.of(9, 0), responses.get(1).endTime());

        verify(professionalRepository).findById(professionalId);
        verify(searchProfessionalAvailabilityTimesUseCase).executeUseCase(professional, date);
    }

    @Test
    void getAvailabilityTimesFromProfessionalShouldEmptyList() {

        Long professionalId = 1L;
        LocalDate date = LocalDate.of(2024, 3, 25);
        Professional professional = mock(Professional.class);

        given(professionalRepository.findById(professionalId)).willReturn(Optional.of(professional));
        given(searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional, date)).willReturn(Collections.emptyList());

        List<TimeSlotResponse> responses = professionalService.getAvailabilityTimesFromProfessional(professionalId, date);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(professionalRepository).findById(professionalId);
        verify(searchProfessionalAvailabilityTimesUseCase).executeUseCase(professional, date);
    }

    private List<TimeSlot> createTimeSlots(boolean available) {

        return List.of(new TimeSlot(LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), available),
                       new TimeSlot(LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"), available));
    }

    @Test
    void getAvailabilityDaysFromProfessionalShouldReturnAvailabilityDaysFromProfessional() {

        long professionalId = 1L;
        int month = 3;
        int year = LocalDate.now().plusYears(1).getYear();
        List<Integer> availableDays = List.of(5, 10, 15, 20);

        given(professionalRepository.existsById(anyLong())).willReturn(true);
        lenient().when(searchProfessionalAvailabilityDaysUseCase.executeUseCase(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(availableDays);

        List<Integer> result = professionalService.getAvailabilityDaysFromProfessional(professionalId, month, year);

        assertNotNull(result);
        assertEquals(availableDays.size(), result.size());
        assertTrue(result.contains(5));
        assertTrue(result.contains(10));
        assertFalse(result.contains(12));

        verify(professionalRepository).existsById(anyLong());
        verify(searchProfessionalAvailabilityDaysUseCase).executeUseCase(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getAvailabilityDaysFromProfessionalShouldEmptyList() {

        long professionalId = 1L;
        int month = 3;
        int year = LocalDate.now().plusYears(1).getYear();
        List<Integer> availableDays = List.of();

        given(professionalRepository.existsById(anyLong())).willReturn(true);
        lenient().when(searchProfessionalAvailabilityDaysUseCase.executeUseCase(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(availableDays);

        List<Integer> result = professionalService.getAvailabilityDaysFromProfessional(professionalId, month, year);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(professionalRepository).existsById(anyLong());
        verify(searchProfessionalAvailabilityDaysUseCase).executeUseCase(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }
}
