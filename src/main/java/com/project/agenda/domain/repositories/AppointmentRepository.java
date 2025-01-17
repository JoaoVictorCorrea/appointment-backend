package com.project.agenda.domain.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.agenda.domain.entities.Appointment;
import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.entities.Professional;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
       @Query("SELECT COUNT(a) > 0 FROM Appointment a " +
              "WHERE a.client = :client " +
              "AND a.date = :date " +
              "AND a.startTime < :endTime " + 
              "AND a.endTime > :startTime " +
              "AND ( " +
              "       a.status = com.project.agenda.domain.entities.AppointmentStatus.OPEN OR " +
              "       a.status = com.project.agenda.domain.entities.AppointmentStatus.PRESENT " +
              ")"
             )
       boolean existsOpenOrPresentAppointmentsForClient(Client client, LocalDate date, LocalTime startTime, LocalTime endTime);
            
       @Query("SELECT COUNT(a) > 0 FROM Appointment a " +
              "WHERE a.professional = :professional " +
              "AND a.date = :date " +
              "AND a.startTime < :endTime " + 
              "AND a.endTime > :startTime " +
              "AND ( " +
              "       a.status = com.project.agenda.domain.entities.AppointmentStatus.OPEN OR " +
              "       a.status = com.project.agenda.domain.entities.AppointmentStatus.PRESENT " +
              ")"
             )
       boolean existsOpenOrPresentAppointmentsForProfessional(Professional professional, LocalDate date, LocalTime startTime, LocalTime endTime);
       
       List<Appointment> findByProfessionalIdAndDate(Long id, LocalDate date);

    @Query(value="SELECT EXTRACT(DOW FROM DATE(:start)) ", nativeQuery=true)
    Integer testeNative( LocalDate start);

    //Native Queries
    public List<Integer> getAvailableDaysFromProfessional(long professionalId, LocalDate start, LocalDate end);
}
