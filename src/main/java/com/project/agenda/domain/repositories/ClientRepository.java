package com.project.agenda.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.agenda.domain.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    public Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
