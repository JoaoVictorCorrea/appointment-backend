package com.project.agenda.dto;

import java.time.LocalDate;

public record ClientResponse(
    long id,
    String name,
    String phone,
    LocalDate dateOfBirth,
    String comments) {
}
