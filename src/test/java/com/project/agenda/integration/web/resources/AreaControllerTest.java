package com.project.agenda.integration.web.resources;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AreaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAreasTest_OK() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/areas"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(1)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("Clínico Geral")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(2)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.equalTo("Fisioterapeuta")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.equalTo(3)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.equalTo("Cardiologista")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").exists())
              .andExpect(MockMvcResultMatchers.jsonPath("$[*].name").exists());
    }

    @Test
    public void getProfessionalsByAreaTest_OK() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/areas/1/professionals"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(6)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("Daniel Oliveira")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone", Matchers.equalTo("13 111222333")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].active", Matchers.equalTo(true)));

        result = mockMvc.perform(MockMvcRequestBuilders.get("/areas/2/professionals"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(8)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("Gustavo Pereira")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone", Matchers.equalTo("21 777888999")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].active", Matchers.equalTo(true)));

        result = mockMvc.perform(MockMvcRequestBuilders.get("/areas/3/professionals"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(6)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("Daniel Oliveira")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone", Matchers.equalTo("13 111222333")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0].active", Matchers.equalTo(true)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(8)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.equalTo("Gustavo Pereira")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1].phone", Matchers.equalTo("21 777888999")))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1].active", Matchers.equalTo(true)));
    }

    @Test
    public void getProfessionalsByAreaTest_NotFound() throws Exception{
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/areas/4/professionals"));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
