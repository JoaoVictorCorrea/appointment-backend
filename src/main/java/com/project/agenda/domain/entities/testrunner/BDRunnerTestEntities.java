package com.project.agenda.domain.entities.testrunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.project.agenda.domain.entities.Appointment;
import com.project.agenda.domain.entities.AppointmentType;
import com.project.agenda.domain.entities.Area;
import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.entities.WorkScheduleItem;
import com.project.agenda.domain.repositories.AppointmentRepository;
import com.project.agenda.domain.repositories.AppointmentTypeRepository;
import com.project.agenda.domain.repositories.AreaRepository;
import com.project.agenda.domain.repositories.ClientRepository;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import com.project.agenda.domain.repositories.WorkScheduleItemRepository;

@Component
@Profile("runner")
public class BDRunnerTestEntities implements ApplicationRunner {
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private WorkScheduleItemRepository workScheduleItemRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
//        Client c1 = clientRepository.findById(1L).get();
//        System.out.println(c1);
//
//        Professional p1 = professionalRepository.findById(4L).get();
//        System.out.println(p1);
//
//        Area a1 = areaRepository.findById(3).get();
//        System.out.println(a1);
//
//        AppointmentType apt1 = appointmentTypeRepository.findById(1).get();
//        System.out.println(apt1);
//
//        WorkScheduleItem ws = new WorkScheduleItem(DayOfWeek.MONDAY, LocalTime.parse("08:00:00"),LocalTime.parse("12:00:00"), 8, 30);
//        this.workScheduleItemRepository.save(ws);
//
//        Appointment appointment = new Appointment();
//        appointment.setClient(c1);
//        appointment.setProfessional(p1);
//        appointment.setArea(a1);
//        appointment.setAppointmentType(apt1);
//        appointment.setDate(LocalDate.of(2024, 06, 17));
//        appointment.setStartTime(LocalTime.of(10, 00));
//        appointment.setEndTime(LocalTime.of(10, 30));
//        appointment.setComments("Urgente!");
//
//        appointmentRepository.save(appointment);

        WorkScheduleItem ws = new WorkScheduleItem(DayOfWeek.MONDAY, LocalTime.parse("08:00:00"),LocalTime.parse("12:00:00"), 8, 30);
        this.workScheduleItemRepository.save(ws);
    }
}
