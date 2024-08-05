package com.project.agenda.domain.mappers;

import java.util.stream.Collectors;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.dto.ProfessionalRequest;
import com.project.agenda.dto.ProfessionalResponse;
import com.project.agenda.dto.ProfessionalWithAreaResponse;

public class ProfessionalMapper {
    
    public static ProfessionalResponse toProfessionalResponseDTO(Professional professional) {

        return new ProfessionalResponse(
                professional.getId(),
                professional.getName(),
                professional.getPhone(),
                professional.isActive());
    }

    public static ProfessionalWithAreaResponse toProfessionalWithAreaResponseDTO(Professional professional) {

        return new ProfessionalWithAreaResponse(
                professional.getId(),
                professional.getName(),
                professional.getPhone(),
                professional.isActive(),
                professional.getAreas().stream().map(a -> AreaMapper.toAreaResponseDTO(a)).collect(Collectors.toSet()));
    }
    
    public static Professional fromProfessionalRequestDTO(ProfessionalRequest professionalRequest) {
        
        return new Professional(
                professionalRequest.name(),
                professionalRequest.phone(),
                professionalRequest.active());
    }
}
