package com.project.agenda.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.agenda.domain.mappers.AppointmentTypeMapper;
import com.project.agenda.domain.repositories.AppointmentTypeRepository;
import com.project.agenda.dto.AppointmentTypeResponse;

@Service
public class AppointmentTypeService {

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Transactional(readOnly = true)
    public List<AppointmentTypeResponse> getAppointmentTypes() {
        
        return appointmentTypeRepository.findAll()
                .stream()
                .map(a -> AppointmentTypeMapper.toAppointmentTypeResponseDTO(a))
                .collect(Collectors.toList());
    }
    
}
