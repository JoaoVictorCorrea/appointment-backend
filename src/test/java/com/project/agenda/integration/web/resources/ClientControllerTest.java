package com.project.agenda.integration.web.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.agenda.dto.ClientRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveClientTest_Created() throws Exception{
        ClientRequest clientRequest = new ClientRequest("Jonathan Camargo", "11 992238200", LocalDate.parse("1978-10-21"), "No comments");
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                                                                     .contentType(MediaType.APPLICATION_JSON)
                                                                     .content(objectMapper.writeValueAsString(clientRequest)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.header().exists("Location"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
              .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(clientRequest.name())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.phone", Matchers.equalTo(clientRequest.phone())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth", Matchers.equalTo(clientRequest.dateOfBirth().toString())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.comments", Matchers.equalTo(clientRequest.comments())));
    }

    @Test
    public void saveClientTest_UnprocessableEntity() throws Exception{
        ClientRequest clientRequest = new ClientRequest("", "", null, "No comments");
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(clientRequest)));

        result.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
              .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
              .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(3)));
    }

    @Test
    public void getPostGetTest_Success() throws Exception{
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/clients"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(3)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id", Matchers.containsInAnyOrder(3, 4, 5)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].name", Matchers.containsInAnyOrder("Alice Silva", "Carlos Souza", "Bruna Lima")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].phone", Matchers.containsInAnyOrder("11 123456789", "11 987654321", "15 555666777")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dateOfBirth", Matchers.containsInAnyOrder("1990-01-15", "1985-05-20", "1992-07-25")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].comments", Matchers.containsInAnyOrder(Matchers.nullValue(), Matchers.nullValue(), Matchers.nullValue())));

        ClientRequest clientRequest = new ClientRequest("Jonathan Camargo", "11 992238200", LocalDate.parse("1978-10-21"), "No comments");
        result = mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.header().exists("Location"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
              .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(clientRequest.name())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.phone", Matchers.equalTo(clientRequest.phone())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth", Matchers.equalTo(clientRequest.dateOfBirth().toString())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.comments", Matchers.equalTo(clientRequest.comments())));

        result = mockMvc.perform(MockMvcRequestBuilders.get("/clients"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(4)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id", Matchers.containsInAnyOrder(3, 4, 5, 9)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].name", Matchers.containsInAnyOrder("Alice Silva", "Carlos Souza", "Bruna Lima", "Jonathan Camargo")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].phone", Matchers.containsInAnyOrder("11 123456789", "11 987654321", "15 555666777", "11 992238200")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dateOfBirth", Matchers.containsInAnyOrder("1990-01-15", "1985-05-20", "1992-07-25", "1978-10-21")))
              .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].comments", Matchers.containsInAnyOrder(Matchers.nullValue(), Matchers.nullValue(), Matchers.nullValue(), Matchers.equalTo("No comments"))));
    }
}
