package com.project.agenda.unit.domain.entities;

import com.project.agenda.domain.entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessionalTest {

    @Test
    void getActiveShouldReturnFalse(){
        Professional p = new Professional();

        assertFalse(p.isActive());
    }

    @Test
    void getActiveShouldReturnTrue(){
        Professional p = new Professional();
        p.setActive(true);

        assertTrue(p.isActive());
    }

    @Test
    void constructorShouldSetAllAttributes(){

        String expName = "Jones";
        String expPhone = "11 123456789";
        boolean expActive = true;

        Set<Area> expAreas = new HashSet<>();
        expAreas.add(new Area(1));
        expAreas.add(new Area(2));

        List<Appointment> expAppointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.parse("08:00:00"));
        appointment.setEndTime(LocalTime.parse("08:30:00"));
        appointment.setComments("comentários");
        appointment.setAppointmentType(new AppointmentType());
        appointment.setArea(new Area());
        appointment.setProfessional(new Professional());
        appointment.setClient(new Client());

        expAppointments.add(appointment);

        Professional p = new Professional(expName, expPhone, expActive, expAreas, expAppointments);

        assertEquals(expName, p.getName());
        assertEquals(expPhone, p.getPhone());
        assertEquals(expActive, p.isActive());
        assertEquals(expAreas, p.getAreas());
        assertEquals(expAppointments, p.getAppointments());
        assertNull(p.getId());
    }

    @Test
    void getAppointmentsShouldReturnEmptyList(){
        Professional p = new Professional();

        assertTrue(p.getAppointments().isEmpty());
    }

    @Test
    void getAppointmentsShouldReturnListOfAppointments(){
        Professional p = new Professional();

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.parse("08:00:00"));
        appointment.setEndTime(LocalTime.parse("08:30:00"));
        appointment.setComments("comentários");
        appointment.setAppointmentType(new AppointmentType());
        appointment.setArea(new Area());
        appointment.setProfessional(p);
        appointment.setClient(new Client());

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        p.setAppointments(appointments);

        assertFalse(p.getAppointments().isEmpty());
    }
}
