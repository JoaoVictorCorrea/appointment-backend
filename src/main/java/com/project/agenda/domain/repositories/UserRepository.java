package com.project.agenda.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.agenda.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
