package com.project.agenda.web.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.agenda.domain.services.AppointmentService;
import com.project.agenda.dto.AppointmentRequest;
import com.project.agenda.dto.AppointmentResponse;

@RestController
@RequestMapping("appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Validated @RequestBody AppointmentRequest appointmentRequest) {

        AppointmentResponse appointmentResponse = appointmentService.createAppointment(appointmentRequest);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointmentResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(appointmentResponse);
    }
}
