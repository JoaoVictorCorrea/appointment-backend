package com.project.agenda.integration.web.resources;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfessionalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAvailabilityTimes_WithAppointments_Ok() throws Exception{
        //Professional with id = 6 works in this WeekDay (Friday) and have appointments (See data.sql)
        LocalDate dateFuture = LocalDate.parse("2028-01-07");
        int PROFESSIONAL_ID = 6;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/professionals/" + PROFESSIONAL_ID +"/availability-times").param("date", dateFuture.toString()));

        String[] times = {"14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00"};
        Boolean[] available = {false, false, false, false};
        for (int i = 0; i < 4; i++){
            result.andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].startTime").value(times[i]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].endTime").value(times[i + 1]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].available").value(available[i]));
        }

        dateFuture = LocalDate.parse("2028-01-14");
        result = mockMvc.perform(MockMvcRequestBuilders.get("/professionals/" + PROFESSIONAL_ID +"/availability-times").param("date", dateFuture.toString()));
        available = Arrays.array(true, true, true, false);
        for (int i = 0; i < 4; i++){
            result.andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].startTime").value(times[i]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].endTime").value(times[i + 1]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].available").value(available[i]));
        }
    }

    @Test
    public void getAvailabilityTimes_WithoutAppointments_Ok() throws Exception{
        //Professional with id = 6 works in this WeekDay (Thursday) and does not have appointments (See data.sql)
        LocalDate dateFuture = LocalDate.now().plusWeeks(100).with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
        int PROFESSIONAL_ID = 6;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/professionals/" + PROFESSIONAL_ID +"/availability-times").param("date", dateFuture.toString()));

        String[] morningTimes = {"08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00"};
        for (int i = 0; i < 8; i++){
            result.andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].startTime").value(morningTimes[i]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].endTime").value(morningTimes[i + 1]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].available").value(true));
        }

        String[] afternoonTimes = {"14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00", "18:00:00"};
        for (int i = 0; i < 8; i++){
            result.andExpect(MockMvcResultMatchers.jsonPath("$[" + (i + 8) + "].startTime").value(afternoonTimes[i]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + (i + 8) + "].endTime").value(afternoonTimes[i + 1]))
                  .andExpect(MockMvcResultMatchers.jsonPath("$[" + (i + 8) + "].available").value(true));
        }
    }

    @Test
    public void getAvailabilityTimes_EmptyList_Ok() throws Exception{
        //Professional with id = 6 does not work in this WeekDay (Tuesday) (See data.sql)
        LocalDate dateFuture = LocalDate.now().plusWeeks(100).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
        int PROFESSIONAL_ID = 6;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/professionals/" + PROFESSIONAL_ID +"/availability-times").param("date", dateFuture.toString()));

        result.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }
}
    