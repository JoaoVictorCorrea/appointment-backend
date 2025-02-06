package com.project.agenda.unit.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.project.agenda.domain.entities.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class ClientTest {

    @Test
    void getDateOfBirthShouldReturnNull(){
        Client c = new Client();

        assertNull(c.getDateOfBirth());
    }

    @Test
    void getDateOfBirthShouldReturnLocalDate(){
        Client c = new Client();
        LocalDate expDate = LocalDate.parse("2025-01-03");

        c.setDateOfBirth(expDate);

        assertEquals(expDate, c.getDateOfBirth());
    }

    @Test
    void constructorShouldSetAllAttributes(){

        LocalDate expDate = LocalDate.parse("2025-01-03");
        String expName = "Rafaela";
        String expComments = "none";
        String expPhone = "11 123456789";

        Client c = new Client(expName, expPhone, expDate, expComments);

        assertEquals(expName, c.getName());
        assertEquals(expPhone, c.getPhone());
        assertEquals(expDate, c.getDateOfBirth());
        assertEquals(expComments, c.getComments());
        assertNull(c.getId());
    }
}
