package com.wasp203.core_service.dto;

import java.time.LocalDate;

public record CreateStudentRequest(String name, LocalDate dateOfBirth, String groupNumber) {
}
