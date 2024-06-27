package com.project.agenda.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.project.agenda.domain.entities.Client;
import com.project.agenda.domain.mappers.ClientMapper;
import com.project.agenda.domain.repositories.ClientRepository;
import com.project.agenda.domain.services.exceptions.DatabaseException;
import com.project.agenda.dto.ClientRequest;
import com.project.agenda.dto.ClientResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;

    public Page<ClientResponse> findByNameContainingIgnoreCase(String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> pageClient = clientRepository.findByNameContainingIgnoreCase(name, pageRequest);

        return pageClient.map(c -> ClientMapper.toClientResponseDTO(c));
    }

    public ClientResponse getById(long id) {

        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        return ClientMapper.toClientResponseDTO(client);
    }
    
    public ClientResponse save(ClientRequest clientRequest) {

        Client client = clientRepository.save(ClientMapper.fromClientRequestDTO(clientRequest));

        return ClientMapper.toClientResponseDTO(client);
    }
    
    public void update(long id, ClientRequest clientRequest) {

        try {
            Client client = clientRepository.getReferenceById(id);

            client.setName(clientRequest.name());
            client.setPhone(clientRequest.phone());
            client.setDateOfBirth(clientRequest.dateOfBirth());

            clientRepository.save(client);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cliente não encontrado.");
        }
    }
    
    public void deleteById(long id) {

        try{    
            if(clientRepository.existsById(id)){
                clientRepository.deleteById(id);
            }
            else{
                throw new EntityNotFoundException("Cliente não encontrado.");
            }
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Conflito ao remover o cliente.");
        }
    }
}
