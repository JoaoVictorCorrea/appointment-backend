package com.project.agenda.web.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.agenda.domain.services.AppointmentTypeService;
import com.project.agenda.dto.AppointmentTypeResponse;

@RestController
@RequestMapping("appointment-types")
public class AppointmentTypeController {
    
    @Autowired
    private AppointmentTypeService appointmentTypeService;

    @GetMapping
    public ResponseEntity<List<AppointmentTypeResponse>> getAppointmentTypes() {
        
        return ResponseEntity.ok(appointmentTypeService.getAppointmentTypes());
    }
}
