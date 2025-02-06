package com.project.agenda.unit.domain.services.usecases.write;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;
import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.agenda.domain.entities.*;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.*;
import com.project.agenda.domain.services.exceptions.BusinessException;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
import com.project.agenda.domain.services.usecases.write.CreateAppointmentUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class CreateAppointmentUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentTypeRepository appointmentTypeRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;

    @InjectMocks
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Test
    void executeUseCaseShouldPersistAppointment(){

        AppointmentType appointmentType = mock(AppointmentType.class);
        given(appointmentTypeRepository.existsById(anyInt())).willReturn(true);

        Area area = mock(Area.class);
        given(areaRepository.existsById(anyInt())).willReturn(true);

        Professional professional = mock(Professional.class);
        given(professional.isActive()).willReturn(true);
        given(professionalRepository.findById(anyLong())).willReturn(of(professional));
        given(professionalRepository.existsAssociationWithArea(anyLong(), anyInt())).willReturn(true);

        Client client = mock(Client.class);
        given(clientRepository.findById(anyLong())).willReturn(of(client));

        given(appointmentRepository.existsOpenOrPresentAppointmentsForProfessional(any(Professional.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).willReturn(false);
        given(appointmentRepository.existsOpenOrPresentAppointmentsForClient(any(Client.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).willReturn(false);

        List<TimeSlot> timeSlots = createTimeSlots(true);
        given(searchProfessionalAvailabilityTimesUseCase.executeUseCase(any(Professional.class), any(LocalDate.class))).willReturn(timeSlots);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.parse("08:00:00"));
        appointment.setEndTime(LocalTime.parse("08:30:00"));
        appointment.setComments("comentários");
        appointment.setAppointmentType(appointmentType);
        appointment.setArea(area);
        appointment.setProfessional(professional);
        appointment.setClient(client);

        given(appointmentRepository.save(any(Appointment.class))).willReturn(appointment);

        createAppointmentUseCase.executeUseCase(appointment);

        verify(appointmentTypeRepository).existsById(any());
        verify(areaRepository).existsById(any());
        verify(professionalRepository).findById(any());
        verify(professionalRepository).existsAssociationWithArea(any(), any());
        verify(searchProfessionalAvailabilityTimesUseCase).executeUseCase(any(), any());
        verify(clientRepository).findById(any());
        verify(appointmentRepository).save(any());
    }

    private List<TimeSlot> createTimeSlots(boolean available) {

        return List.of(new TimeSlot(LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"), available));
    }

    @Test
    void executeUseCaseShouldThrowsBusinessException_WrongStartAndEndTime(){

        AppointmentType appointmentType = mock(AppointmentType.class);
        lenient().when(appointmentTypeRepository.existsById(anyInt())).thenReturn(true);

        Area area = mock(Area.class);
        lenient().when(areaRepository.existsById(anyInt())).thenReturn(true);

        Professional professional = mock(Professional.class);
        lenient().when(professional.isActive()).thenReturn(true);
        lenient().when(professionalRepository.findById(anyLong())).thenReturn(of(professional));
        lenient().when(professionalRepository.existsAssociationWithArea(anyLong(), anyInt())).thenReturn(true);

        Client client = mock(Client.class);
        lenient().when(clientRepository.findById(anyLong())).thenReturn(of(client));

        lenient().when(appointmentRepository.existsOpenOrPresentAppointmentsForProfessional(any(Professional.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(false);
        lenient().when(appointmentRepository.existsOpenOrPresentAppointmentsForClient(any(Client.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(false);

        List<TimeSlot> timeSlots = createTimeSlots(true);
        given(searchProfessionalAvailabilityTimesUseCase.executeUseCase(any(Professional.class), any(LocalDate.class))).willReturn(timeSlots);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.parse("2025-05-02"));
        appointment.setStartTime(LocalTime.parse("08:15:00"));
        appointment.setEndTime(LocalTime.parse("08:30:00"));
        appointment.setComments("comentários");
        appointment.setAppointmentType(appointmentType);
        appointment.setArea(area);
        appointment.setProfessional(professional);
        appointment.setClient(client);

        assertThrows(BusinessException.class, () -> createAppointmentUseCase.executeUseCase(appointment));

        verify(searchProfessionalAvailabilityTimesUseCase).executeUseCase(any(Professional.class), any(LocalDate.class));
        verify(appointmentRepository, never()).save(any()); //verifica se nunca foi chamado
    }

    @Test
    void executeUseCaseShouldThrowsBusinessException_ProfessionalNotAvailable(){

        AppointmentType appointmentType = mock(AppointmentType.class);
        lenient().when(appointmentTypeRepository.existsById(anyInt())).thenReturn(true);

        Area area = mock(Area.class);
        lenient().when(areaRepository.existsById(anyInt())).thenReturn(true);

        Professional professional = mock(Professional.class);
        lenient().when(professional.isActive()).thenReturn(true);
        lenient().when(professionalRepository.findById(anyLong())).thenReturn(of(professional));
        lenient().when(professionalRepository.existsAssociationWithArea(anyLong(), anyInt())).thenReturn(true);

        Client client = mock(Client.class);
        lenient().when(clientRepository.findById(anyLong())).thenReturn(of(client));

        lenient().when(appointmentRepository.existsOpenOrPresentAppointmentsForProfessional(any(Professional.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(true);
        lenient().when(appointmentRepository.existsOpenOrPresentAppointmentsForClient(any(Client.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(false);

        List<TimeSlot> timeSlots = createTimeSlots(true);
        given(searchProfessionalAvailabilityTimesUseCase.executeUseCase(any(Professional.class), any(LocalDate.class))).willReturn(timeSlots);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.parse("08:00:00"));
        appointment.setEndTime(LocalTime.parse("08:30:00"));
        appointment.setComments("comentários");
        appointment.setAppointmentType(appointmentType);
        appointment.setArea(area);
        appointment.setProfessional(professional);
        appointment.setClient(client);

        assertThrows(BusinessException.class, () -> createAppointmentUseCase.executeUseCase(appointment));

        verify(searchProfessionalAvailabilityTimesUseCase).executeUseCase(any(Professional.class), any(LocalDate.class));
        verify(appointmentRepository, never()).save(any()); //verifica se nunca foi chamado
    }
}
