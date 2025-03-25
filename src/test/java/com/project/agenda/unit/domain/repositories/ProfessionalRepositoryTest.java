package com.project.agenda.unit.domain.repositories;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.repositories.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnabledIf(expression = "#{environment.acceptsProfiles('test')}", loadContext = true)
public class ProfessionalRepositoryTest {

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Test
    void existsAssociationWithAreaShouldReturnTrue(){

        boolean exp;

        exp = professionalRepository.existsAssociationWithArea(6l, 1);
        assertTrue(exp);

        exp = professionalRepository.existsAssociationWithArea(6l, 3);
        assertTrue(exp);

        exp = professionalRepository.existsAssociationWithArea(7l, 1);
        assertTrue(exp);

        exp = professionalRepository.existsAssociationWithArea(7l, 2);
        assertTrue(exp);

        exp = professionalRepository.existsAssociationWithArea(8l, 2);
        assertTrue(exp);

        exp = professionalRepository.existsAssociationWithArea(8l, 3);
        assertTrue(exp);
    }

    @Test
    void existsAssociationWithAreaShouldReturnFalse(){

        boolean exp;

        exp = professionalRepository.existsAssociationWithArea(6l, 2);
        assertFalse(exp);

        exp = professionalRepository.existsAssociationWithArea(7l, 3);
        assertFalse(exp);

        exp = professionalRepository.existsAssociationWithArea(8l, 1);
        assertFalse(exp);
    }

    @Test
    void findByNameContainingIgnoreCaseShouldReturnPageOfProfessional(){

        String name = "an"; //exists 2 professionals containing 'an': 'Daniel Oliveira' and 'Fernanda Costa'
        int page = 0;
        int size = 10;

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Professional> pageProfessional = professionalRepository.findByNameContainingIgnoreCase(name, pageRequest);

        assertFalse(pageProfessional.isEmpty());
        assertTrue(pageProfessional.getContent().stream()
                .allMatch(professional -> professional.getName().toLowerCase().contains(name.toLowerCase())));

        assertEquals(page, pageProfessional.getNumber());
        assertEquals(size, pageProfessional.getSize());
        assertEquals(2, pageProfessional.getNumberOfElements());
    }

    @Test
    void findByNameContainingIgnoreCaseShouldReturnEmptyPageWhenNoMatch(){

        String name = "Joãozinho123";
        int page = 0;
        int size = 10;

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Professional> pageProfessional = professionalRepository.findByNameContainingIgnoreCase(name, pageRequest);

        assertTrue(pageProfessional.isEmpty());
    }
}
