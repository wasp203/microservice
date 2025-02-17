package com.wasp203.core_service.dto;

import java.time.LocalDate;

public record UpdateStudentRequest(Long id, String name, LocalDate dateOfBirth, String groupNumber) {
}
