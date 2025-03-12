package com.wasp203.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CreateStudentRequest(String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth, String groupNumber) {
}
