package com.project.agenda.domain.services.exceptions;

public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String message) {
        super(message);
    }
}
