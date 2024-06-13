package com.project.agenda.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.agenda.domain.entities.Area;

public interface AreaRepository extends JpaRepository<Area, Integer>{
    
}
