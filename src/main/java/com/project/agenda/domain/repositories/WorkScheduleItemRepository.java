package com.project.agenda.domain.repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.entities.WorkScheduleItem;

public interface WorkScheduleItemRepository extends JpaRepository<WorkScheduleItem, Long> {

    @Query("SELECT w FROM WorkScheduleItem w " +
           "WHERE w.professional = :professional AND " +
           "      w.dayOfWeek = :dayOfWeek " +
           "ORDER BY w.startTime")
    List<WorkScheduleItem> getWorkScheduleFromProfessionalByDayOfWeekOrderByStartTime(Professional professional, DayOfWeek dayOfWeek);
}
