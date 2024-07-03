package com.project.agenda.domain.services.usecases.read;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.agenda.domain.entities.Appointment;
import com.project.agenda.domain.entities.AppointmentStatus;
import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.entities.WorkScheduleItem;
import com.project.agenda.domain.models.TimeSlot;
import com.project.agenda.domain.repositories.AppointmentRepository;
import com.project.agenda.domain.repositories.WorkScheduleItemRepository;

@Service
public class SearchProfessionalAvailabilityTimesUseCase {

    @Autowired
    private WorkScheduleItemRepository workScheduleItemRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public List<TimeSlot> executeUseCase(Professional professional, LocalDate date) {

        List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
        List<WorkScheduleItem> workScheduleItems = getWorkScheduleItems(professional, date);
        List<Appointment> appointments = getAppointments(professional, date);

        for (var item : workScheduleItems) {
            timeSlots.addAll(calculateTimeSlots(item, appointments, date));
        }

        return timeSlots;
    }
    
    private List<WorkScheduleItem> getWorkScheduleItems(Professional professional, LocalDate date) {
        return this.workScheduleItemRepository.getWorkScheduleFromProfessionalByDayOfWeekOrderByStartTime(professional, date.getDayOfWeek());
    }
    
    private List<Appointment> getAppointments(Professional professional, LocalDate date) {
        return this.appointmentRepository.findByProfessionalIdAndDate(professional.getId(), date);
    }

    private List<TimeSlot> calculateTimeSlots(WorkScheduleItem item, List<Appointment> appointments, LocalDate date) {

        LocalTime startTime = item.getStartTime();
        Integer slotSize = item.getSlotSize();
        Integer slots = item.getSlots();

        List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();

        for (int i = 0; i < slots; i++) {

            LocalTime start = startTime.plusMinutes(slotSize * i);
            LocalTime end = start.plusMinutes(slotSize);

            boolean available = isTimeSlotAvailable(start, end, appointments);
            boolean nowOrFuture = isStartTimeValidIfDateIsToday(start, date);

            timeSlots.add(new TimeSlot(start, end, available && nowOrFuture));
        }

        return timeSlots;
    }

    /*
        Time Slot (09:00-09:30)
        None Match: Appointments: (08:00-08:30), (08:30-09:00), (09:30-10:00)
        Match: Appointments: (09:10-09:20), (08:00-10:00), (09:10-10:00), (08:00-09:10)
     */
    private boolean isTimeSlotAvailable(LocalTime start, LocalTime end, List<Appointment> appointments) {

        return appointments.stream().noneMatch(a -> a.getStartTime().isBefore(end) &&
                a.getEndTime().isAfter(start) &&
                (a.getStatus().equals(AppointmentStatus.OPEN) || a.getStatus().equals(AppointmentStatus.PRESENT)));
    }
    
    private boolean isStartTimeValidIfDateIsToday(LocalTime start, LocalDate date) {
        
        return (date.isAfter(LocalDate.now())) || (date.equals(LocalDate.now()) && start.isAfter(LocalTime.now()));
    }
}
