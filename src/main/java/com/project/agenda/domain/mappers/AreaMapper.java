package com.project.agenda.domain.mappers;

import com.project.agenda.domain.entities.Area;
import com.project.agenda.dto.AreaResponse;

public class AreaMapper {
    
    public static AreaResponse toAreaResponseDTO(Area area) {

        return new AreaResponse(area.getId(), area.getName());
    }
}
