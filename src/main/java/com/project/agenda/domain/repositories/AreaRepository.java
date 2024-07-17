package com.project.agenda.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.agenda.domain.entities.Area;
import com.project.agenda.domain.entities.Professional;

public interface AreaRepository extends JpaRepository<Area, Integer> {
    
    @Query("SELECT p FROM Professional p JOIN p.areas a WHERE a.id = :areaId AND p.active = true")
    List<Professional> findActiveProfessionalsById(int areaId);
}
