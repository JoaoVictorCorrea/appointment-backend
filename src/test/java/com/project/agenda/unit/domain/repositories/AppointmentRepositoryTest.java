package com.project.agenda.unit.domain.repositories;

import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.repositories.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnabledIf(expression = "#{environment.acceptsProfiles('test')}", loadContext = true)
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void existsOpenOrPresentAppointmentsForClientShouldReturnTrue(){

        Client client3 = new Client(3L);
        LocalDate date = LocalDate.parse("2028-08-17");
        LocalTime startTime = LocalTime.parse("08:00:00");
        LocalTime endTime = LocalTime.parse("08:30:00");

        assertTrue(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        startTime = LocalTime.parse("08:01:00");
        endTime = LocalTime.parse("08:02:00");

        assertTrue(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        startTime = LocalTime.parse("07:00:00");
        endTime = LocalTime.parse("08:02:00");

        assertTrue(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        startTime = LocalTime.parse("05:30:00");
        endTime = LocalTime.parse("08:29:00");

        assertTrue(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        //testar com outros clients
    }

    @Test
    void existsOpenOrPresentAppointmentsForClientShouldReturnFalse(){

        Client client3 = new Client(3L);
        LocalDate date = LocalDate.parse("2028-08-18");
        LocalTime startTime = LocalTime.parse("08:00:00");
        LocalTime endTime = LocalTime.parse("08:30:00");

        assertFalse(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        date = LocalDate.parse("2028-08-17");
        startTime = LocalTime.parse("07:30:00");
        endTime = LocalTime.parse("08:00:00");

        assertFalse(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        startTime = LocalTime.parse("08:30:00");
        endTime = LocalTime.parse("09:00:00");

        assertFalse(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        startTime = LocalTime.parse("08:30:00");
        endTime = LocalTime.parse("08:32:00");

        assertFalse(appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime));

        //testar com outros clients
    }

    @Test
    void getAvailableDaysFromProfessionalShouldReturnListOfInteger(){

        //mês vazio, retorna todas as datas em que ele trabalha

        Professional professional8 = new Professional(8L);
        LocalDate startDate = LocalDate.parse("2025-01-01");
        LocalDate endDate = LocalDate.parse("2025-01-31");
        List<Integer> foundDays = appointmentRepository.getAvailableDaysFromProfessional(professional8.getId(), startDate, endDate);
        List<Integer> expectDays = List.of(1, 3, 8, 10, 15, 17, 22, 24, 29, 31);

        assertIterableEquals(expectDays, foundDays);

        //mês com um dia cheio, retorna todas as outras datas em que ele trabalha

        startDate = LocalDate.parse("2028-01-01");
        endDate = LocalDate.parse("2028-01-31");
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professional8.getId(), startDate, endDate);
        expectDays = List.of(5, 12, 14, 19, 21, 26, 28);

        assertIterableEquals(expectDays, foundDays);

        //testar com outros professionals
    }

    @Test
    void getAvailableDaysFromProfessionalShouldReturnEmptyList(){

        //dia em que o profissional não trabalha.

        Professional professional6 = new Professional(6L);
        LocalDate startDate = LocalDate.parse("2025-01-05");
        LocalDate endDate = LocalDate.parse("2025-01-06");

        assertTrue(appointmentRepository.getAvailableDaysFromProfessional(professional6.getId(), startDate, endDate).isEmpty());

        //dia em que o profissional trabalha, mas com agenda cheia.

        startDate = LocalDate.parse("2028-01-07");
        endDate = LocalDate.parse("2028-01-08");

        assertTrue(appointmentRepository.getAvailableDaysFromProfessional(professional6.getId(), startDate, endDate).isEmpty());

        //testar com outros professionals
    }
}
