package com.project.agenda.domain.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_CLIENT")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
public class Client extends Person {

    private LocalDate dateOfBirth;
    private String comments;

    public Client() {}
    
    public Client(Long id) {
        super(id);
    }

    public Client(String name, String phone, LocalDate dateOfBirth, String comments) {
        super(name, phone);
        this.dateOfBirth = dateOfBirth;
        this.comments = comments;
    }

    @OneToMany(mappedBy = "client")
    private List<Appointment> appointments = new ArrayList<>();

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getComments(){
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Client [dateOfBirth=" + dateOfBirth + " comments=" + comments + " " + super.toString() + "]";
    }
}
