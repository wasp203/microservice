package com.wasp203.api_gateway.dto;

import java.time.LocalDate;

public record CreateStudentRequest(String name, LocalDate dateOfBirth, String groupNumber) {
}
