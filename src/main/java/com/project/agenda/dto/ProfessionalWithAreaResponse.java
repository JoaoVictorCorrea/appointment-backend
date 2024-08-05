package com.project.agenda.dto;

import java.util.Set;

public record ProfessionalWithAreaResponse(
    long id,
    String name,
    String phone,
    boolean active,
    Set<AreaResponse> areas) {
}
