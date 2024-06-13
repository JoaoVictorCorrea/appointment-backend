package com.project.agenda.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.agenda.domain.entities.AppointmentType;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Integer>{
    
}
