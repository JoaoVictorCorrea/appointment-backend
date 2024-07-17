package com.project.agenda.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.domain.mappers.AreaMapper;
import com.project.agenda.domain.mappers.ProfessionalMapper;
import com.project.agenda.domain.repositories.AreaRepository;
import com.project.agenda.dto.AreaResponse;
import com.project.agenda.dto.ProfessionalResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AreaService {
    
    @Autowired
    private AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public List<AreaResponse> getAreas() {

        return areaRepository.findAll()
                .stream()
                .map(a -> AreaMapper.toAreaResponseDTO(a))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfessionalResponse> getProfessionalsByArea(int areaId) {

        if (!areaRepository.existsById(areaId)) {
            throw new EntityNotFoundException("Area não cadastrada.");
        }
        
        List<Professional> professionalsByArea = areaRepository.findActiveProfessionalsById(areaId);
        
        return professionalsByArea
                .stream()
                .map(p -> ProfessionalMapper.toProfessionalResponseDTO(p))
                .collect(Collectors.toList());
    }
}
