package com.project.agenda.domain.mappers;

import com.project.agenda.domain.entities.Professional;
import com.project.agenda.dto.ProfessionalRequest;
import com.project.agenda.dto.ProfessionalResponse;

public class ProfessionalMapper {
    
    public static ProfessionalResponse toProfessionalResponseDTO(Professional professional) {

        return new ProfessionalResponse(
                professional.getId(),
                professional.getName(),
                professional.getPhone(),
                professional.isActive());
    }
    
    public static Professional fromProfessionalRequestDTO(ProfessionalRequest professionalRequest) {
        
        return new Professional(
                professionalRequest.name(),
                professionalRequest.phone(),
                professionalRequest.active());
    }
}
