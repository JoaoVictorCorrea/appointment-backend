package com.project.agenda.unit.domain.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.repositories.AreaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.List;

@DataJpaTest
@EnabledIf(expression = "#{environment.acceptsProfiles('test')}", loadContext = true)
public class AreaRepositoryTest {

    @Autowired
    private AreaRepository areaRepository;

    @Test
    void findActiveProfessionalsByIdShouldReturnProfessionals(){

        int expSizeArea1 = 1;
        int expSizeArea2 = 1;
        int expSizeArea3 = 2;

        List<Professional> professionals;

        professionals = areaRepository.findActiveProfessionalsById(1);
        assertEquals(expSizeArea1, professionals.size());

        professionals = areaRepository.findActiveProfessionalsById(2);
        assertEquals(expSizeArea2, professionals.size());

        professionals = areaRepository.findActiveProfessionalsById(3);
        assertEquals(expSizeArea3, professionals.size());
    }

    @Test
    void findActiveProfessionalsByIdShouldNotReturnProfessionals(){

        int expSizeArea4 = 0;

        List<Professional> professionals;

        professionals = areaRepository.findActiveProfessionalsById(4);
        assertEquals(expSizeArea4, professionals.size());
    }
}
