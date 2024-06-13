package com.project.agenda.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.agenda.domain.entities.WorkScheduleItem;

public interface WorkScheduleItemRepository extends JpaRepository<WorkScheduleItem, Long>{

}
