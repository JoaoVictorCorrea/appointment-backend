package com.project.agenda.web.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.agenda.domain.services.ClientService;
import com.project.agenda.dto.ClientRequest;
import com.project.agenda.dto.ClientResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    
    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getClients(
            @RequestParam(name = "name_like", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(clientService.findByNameContainingIgnoreCase(name, page, size));
    }
    
    @GetMapping("{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable long id) {

        ClientResponse client = clientService.getById(id);

        return ResponseEntity.ok(client);
    }
    
    @PostMapping
    public ResponseEntity<ClientResponse> save(@Validated @RequestBody ClientRequest clientRequest) {

        ClientResponse client = clientService.save(clientRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.id())
                .toUri();

        return ResponseEntity.created(location).body(client);
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Void> updateClient(@PathVariable long id, @Valid @RequestBody ClientRequest clientRequest) {

        clientService.update(id, clientRequest);

        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable long id) {
        
        clientService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
