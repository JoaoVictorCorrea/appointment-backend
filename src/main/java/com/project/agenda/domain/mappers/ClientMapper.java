package com.project.agenda.domain.mappers;

import com.project.agenda.domain.entities.Client;
import com.project.agenda.dto.ClientRequest;
import com.project.agenda.dto.ClientResponse;

public class ClientMapper {
    
    public static ClientResponse toClientResponseDTO(Client client) {

        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getPhone(),
                client.getDateOfBirth(),
                client.getComments());
    }
    
    public static Client fromClientRequestDTO(ClientRequest clientRequest) {
        
        return new Client(
                clientRequest.name(),
                clientRequest.phone(),
                clientRequest.dateOfBirth(),
                clientRequest.comments());
    }
}
