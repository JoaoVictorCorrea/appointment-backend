package com.project.agenda.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.agenda.domain.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    
}
