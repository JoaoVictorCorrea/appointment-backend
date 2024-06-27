package com.project.agenda.domain.services.usecases.write;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.agenda.domain.entities.Appointment;
import com.project.agenda.domain.entities.AppointmentType;
import com.project.agenda.domain.entities.Area;
import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.AppointmentRepository;
import com.project.agenda.domain.repositories.AppointmentTypeRepository;
import com.project.agenda.domain.repositories.AreaRepository;
import com.project.agenda.domain.repositories.ClientRepository;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import com.project.agenda.domain.services.exceptions.BusinessException;
import com.project.agenda.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CreateAppointmentUseCase {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;
    
    public Appointment executeUseCase(Appointment appointment) {

        checkAppointmentTypeExistsOrThrowsException(appointment.getAppointmentType());
        checkAreaExistsOrThrowsException(appointment.getArea());
        
        Professional professional = getProfessionalIfExistsOrThrowsException(appointment.getProfessional());
        checkProfessionalIsActiveOrThrowsException(professional);
        checkAssociationBetweenProfessionalAndAreaOrThrowsException(professional, appointment.getArea());
        checkProfessionalCanCreateAppointmentAtDateAndTimeOrThrowsException(professional, appointment);
        checkProfessionalHasAvailableScheduleOrThrowsException(professional, appointment);

        checkAppointmentIsNowOrFutureOrThrowsException(appointment.getDate(), appointment.getStartTime());
        
        Client client = getClientIfExistsOrThrowsException(appointment.getClient());
        checkClientCanCreateAppointmentAtDateAndTimeOrThrowsException(client, appointment);

        return this.appointmentRepository.save(appointment);
    }

    private void checkAppointmentTypeExistsOrThrowsException(AppointmentType appointmentType) {

        if (!this.appointmentTypeRepository.existsById(appointmentType.getId())) {
            throw new EntityNotFoundException("Tipo de agendamento não cadastrado.");
        }
    }
    
    private void checkAreaExistsOrThrowsException(Area area) {
        if (!this.areaRepository.existsById(area.getId())) {
            throw new EntityNotFoundException("Area não cadastrada.");
        }
    }

    private Professional getProfessionalIfExistsOrThrowsException(Professional professional) {
        return this.professionalRepository.findById(professional.getId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não cadastrado."));
    }

    private void checkProfessionalIsActiveOrThrowsException(Professional professional) {
        if (!professional.isActive()) {
            throw new BusinessException("Profissional desativo.");
        }
    }

    private void checkAssociationBetweenProfessionalAndAreaOrThrowsException(Professional professional, Area area) {
        if (!this.professionalRepository.existsAssociationWithArea(professional.getId(), area.getId())) {
            throw new BusinessException("O profissional não atua na área selecionada.");
        }
    }

    private void checkProfessionalCanCreateAppointmentAtDateAndTimeOrThrowsException(Professional professional, Appointment appointment) {
        if (this.appointmentRepository.existsOpenOrPresentAppointmentsForProfessional(professional,
                appointment.getDate(),
                appointment.getStartTime(), appointment.getEndTime())) {
            throw new BusinessException("O profissional possui agendamentos em aberto para o dia e horário selecionado.");
        }
    }
    
    private void checkProfessionalHasAvailableScheduleOrThrowsException(Professional professional, Appointment appointment) {
        
        List<TimeSlot> timeSlots = searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional, appointment.getDate());
        
        if (timeSlots.isEmpty()) {
            throw new BusinessException("O profissional não trabalha na data selecionada.");
        }
        else {
            Optional<TimeSlot> timeSlot = timeSlots.stream().filter( ts -> ts.getStartTime().equals(appointment.getStartTime()) &&
                                                                           ts.getEndTime().equals(appointment.getEndTime())
                                                                   ).findFirst();
                                                                   
            if (timeSlot.isEmpty()) {
                throw new BusinessException("O profissional não trabalha no horário selecionado.");
            }
        }
    }

    private void checkAppointmentIsNowOrFutureOrThrowsException(LocalDate date, LocalTime startTime) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException("A data do agendamento está no passado.");
        } else {
            if (date.equals(LocalDate.now()) && startTime.isBefore(LocalTime.now())) {
                throw new BusinessException("O horário do agendamento está no passado.");
            }
        }
    }
    
    private Client getClientIfExistsOrThrowsException(Client client) {
        return this.clientRepository.findById(client.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não cadastrado."));
    }

    private void checkClientCanCreateAppointmentAtDateAndTimeOrThrowsException(Client client, Appointment appointment) {
        if (this.appointmentRepository.existsOpenOrPresentAppointmentsForClient(client, appointment.getDate(),
                appointment.getStartTime(), appointment.getEndTime())) {
            throw new BusinessException("O cliente possui agendamentos em aberto para o dia e horário selecionado.");
        }
    }
}