package com.project.agenda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfessionalRequest(
    @NotBlank(message = "Nome requirido") String name,
    @NotBlank(message = "Telefone requirido") String phone,
    @NotNull(message = "Ativo requirido") Boolean active) {
}