package com.project.agenda.unit.domain.services;

import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.repositories.ClientRepository;
import com.project.agenda.domain.services.ClientService;
import com.project.agenda.dto.ClientRequest;
import com.project.agenda.dto.ClientResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void saveShouldPersistClient(){
        Client newClient = new Client("Ana", "119999999", LocalDate.parse("2000-10-01"), "comments");
        Client savedClient = new Client(1L, "Ana", "119999999", LocalDate.parse("2000-10-01"), "comments");

        given(clientRepository.save(newClient)).willReturn(savedClient);

        ClientRequest newClientRequest = new ClientRequest("Ana", "119999999", LocalDate.parse("2000-10-01"), "comments");
        ClientResponse expClientResponse = new ClientResponse(1L, "Ana", "119999999", LocalDate.parse("2000-10-01"), "comments");

        ClientResponse foundClientResponse = clientService.save(newClientRequest);

        verify(clientRepository).save(any(Client.class)); //verifica se o metodo do mock foi ou não chamado

        assertNotNull(foundClientResponse);
        assertNotNull(foundClientResponse.id());

        assertEquals(expClientResponse.id(), foundClientResponse.id());
        assertEquals(expClientResponse.name(), foundClientResponse.name());
        assertEquals(expClientResponse.phone(), foundClientResponse.phone());
        assertEquals(expClientResponse.dateOfBirth(), foundClientResponse.dateOfBirth());
        assertEquals(expClientResponse.comments(), foundClientResponse.comments());
    }

    @Test
    void updateShouldPersistClient(){
        Client client = mock(Client.class);

        given(clientRepository.getReferenceById(anyLong())).willReturn(client);
        given(clientRepository.save(client)).willReturn(client);

        ClientRequest clientRequest = new ClientRequest("Ana", "119999999", LocalDate.parse("2000-10-01"), "comments");

        clientService.update(anyLong(), clientRequest);

        verify(client).setName(anyString());
        verify(client).setPhone(anyString());
        verify(client).setDateOfBirth(any(LocalDate.class));
        verify(client).setComments(anyString());

        verify(clientRepository).save(any(Client.class));
        verify(clientRepository).getReferenceById(anyLong());
    }

    @Test
    void updateShouldThrowsEntityNotFoundException(){
        given(clientRepository.getReferenceById(anyLong())).willThrow(EntityNotFoundException.class);
        ClientRequest clientRequest = mock(ClientRequest.class);

        assertThrows(EntityNotFoundException.class, () -> clientService.update(anyLong(), clientRequest));
        verify(clientRepository).getReferenceById(anyLong());
    }
}
