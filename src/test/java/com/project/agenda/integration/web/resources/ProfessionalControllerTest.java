package com.project.agenda.integration.web.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.agenda.dto.ProfessionalRequest;
import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void getProfessionalsByIdTest_OK() throws Exception {
        final int PROFESSIONAL_ID = 6;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/professionals/" + PROFESSIONAL_ID));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(PROFESSIONAL_ID)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo("Daniel Oliveira")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.phone", Matchers.equalTo("13 111222333")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.active", Matchers.equalTo(true)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.area").exists());
    }

    @Test
    public void getProfessionalsByIdTest_NotFound() throws Exception {
        final int PROFESSIONAL_ID = 99;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/professionals/" + PROFESSIONAL_ID));
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
              .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.equalTo("Resource not found")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Profissional não encontrado.")));
    }

    @Test
    public void saveProfessionalTest_Created() throws Exception{
        ProfessionalRequest professionalRequest = new ProfessionalRequest("Dirceu Assis", "11 992238200", true);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/professionals")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(professionalRequest)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.header().exists("Location"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
              .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(professionalRequest.name())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.phone", Matchers.equalTo(professionalRequest.phone())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.active", Matchers.equalTo(professionalRequest.active())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.area").exists());
    }

    @Test
    public void saveProfessionalTest_UnprocessableEntity() throws Exception{
        ProfessionalRequest professionalRequest = new ProfessionalRequest("", "", null);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/professionals")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(professionalRequest)));

        result.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
              .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(3)));
    }

    @Test
    public void deleteProfessionalByIdTest_WhenNoAssociatedArea_NoContent() throws Exception{
        final int PROFESSIONAL_ID = 7;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/professionals/" + PROFESSIONAL_ID));
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteProfessionalByIdTest_WhenHasAssociatedArea_BadRequest() throws Exception{
        final int PROFESSIONAL_ID = 6;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/professionals/" + PROFESSIONAL_ID));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
              .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.equalTo("Database Integrity Exception")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Conflito com a integração dos dados.")));
    }

    @Test
    public void deleteProfessionalByIdTest_NotFound() throws Exception{
        final int PROFESSIONAL_ID = 99;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/professionals/" + PROFESSIONAL_ID));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
              .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.equalTo("Resource not found")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Profissional não encontrado.")));
    }
}
    