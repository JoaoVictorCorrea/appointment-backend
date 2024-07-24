package com.project.agenda.domain.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_PROFESSIONAL")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
public class Professional extends Person{
    
    private boolean active;

    @ManyToMany
    @JoinTable(name = "TBL_AREA_PROFESSIONAL", 
               joinColumns = @JoinColumn(name = "PROFESSIONAL_ID"), 
               inverseJoinColumns = @JoinColumn(name = "AREA_ID"))
    private Set<Area> areas = new HashSet<>();

    @OneToMany(mappedBy = "professional")
    private List<WorkScheduleItem> workScheduleItens = new ArrayList<>();

    @OneToMany(mappedBy = "professional")
    private List<Appointment> appointments = new ArrayList<>();

    public void addWorkScheduleItem(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, int slots, int slotSize) {

        WorkScheduleItem workScheduleItem = new WorkScheduleItem(dayOfWeek, startTime, endTime, slots, slotSize);
        workScheduleItens.add(workScheduleItem);
    }
    
    public Professional() {}
    
    public Professional(Long id) {
        super(id);
    }

    public Professional(String name, String phone, boolean active) {
        super(name, phone);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Area> getAreas() {
        return areas;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Professional [active=" + active + " " + super.toString() + "]";
    }
}
