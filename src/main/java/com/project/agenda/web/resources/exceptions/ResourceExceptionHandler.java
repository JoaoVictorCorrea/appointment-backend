package com.project.agenda.web.resources.exceptions;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.agenda.domain.services.exceptions.BusinessException;
import com.project.agenda.domain.services.exceptions.DatabaseException;
import com.project.agenda.domain.services.exceptions.ParameterException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ResourceExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrors> validationException(MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        ValidationErrors error = new ValidationErrors();

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        error.setError("Validation Error");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> error.addError(e.getDefaultMessage()));

        return ResponseEntity.status(status).body(error);
    }
    
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException exception,
            HttpServletRequest request) {

        StandardError error = new StandardError();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        error.setError("Database Exception");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> businessException(BusinessException exception,
            HttpServletRequest request) {

        StandardError error = new StandardError();

        HttpStatus status = HttpStatus.CONFLICT;

        error.setError("Business Exception");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFoundException(EntityNotFoundException exception,
            HttpServletRequest request) {

        StandardError error = new StandardError();

        HttpStatus status = HttpStatus.NOT_FOUND;

        error.setError("Resource not found");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<StandardError> dateParseException(DateTimeParseException exception,
            HttpServletRequest request) {

        StandardError error = new StandardError();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        error.setError("Parse Date Exception");
        error.setMessage("Formato de data inválido. Utilize: 'yyyy-MM-dd'");
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrors> constraintViolationException(ConstraintViolationException exception,
            HttpServletRequest request) {

        ValidationErrors error = new ValidationErrors();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        error.setError("Constraint Error");
        error.setMessage("Parâmetros Requiridos.");
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        exception.getConstraintViolations()
                .forEach(e -> error.addError(e.getMessage()));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<StandardError> parameterException(ParameterException exception,
            HttpServletRequest request) {

        StandardError error = new StandardError();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        error.setError("Parameter Exception");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(status.value());
        error.setTimeStamp(Instant.now());

        return ResponseEntity.status(status).body(error);
    }
}
