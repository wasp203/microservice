package com.wasp203.api_gateway.dto;

import java.time.LocalDate;

public record StudentResponse(Long id, String name, LocalDate dateOfBirth, String groupNumber) {
}
