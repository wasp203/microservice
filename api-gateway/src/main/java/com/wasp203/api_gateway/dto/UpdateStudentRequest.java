package com.wasp203.api_gateway.dto;

import java.time.LocalDate;

public record UpdateStudentRequest(Long id, String name, LocalDate dateOfBirth, String groupNumber) {
}
