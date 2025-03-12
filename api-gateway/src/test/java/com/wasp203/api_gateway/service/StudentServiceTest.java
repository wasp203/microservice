package com.wasp203.api_gateway.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.wasp203.api_gateway.dto.CreateStudentRequest;
import com.wasp203.api_gateway.dto.StudentResponse;
import com.wasp203.api_gateway.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 8081)
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("getStudents_ShouldReturnListOfStudents_WhenDataExists")
    void getStudents_ShouldReturnListOfStudents_WhenDataExists() {
        String responseBody = "[{\"id\":1,\"name\":\"Дима Сыров\",\"dateOfBirth\":\"2000-05-15\",\"groupNumber\":\"421\"}]";
        stubFor(get("/api/students")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        List<StudentResponse> result = studentService.getStudents();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).name()).isEqualTo("Дима Сыров");
    }

    @Test
    @DisplayName("getStudent_ShouldReturnStudent_WhenItExists")
    void getStudent_ShouldReturnStudent_WhenItExists() {
        String responseBody = "{\"id\":1,\"name\":\"Дима Сыров\",\"dateOfBirth\":\"2000-05-15\",\"groupNumber\":\"421\"}";
        stubFor(get("/api/students/1")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        StudentResponse result = studentService.getStudent(1L);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Дима Сыров");
    }

    @Test
    @DisplayName("addStudent_ShouldAddStudent_WhenDataIsValid")
    void addStudent_ShouldAddStudent_WhenDataIsValid() {
        CreateStudentRequest request = new CreateStudentRequest("Дима Сыров", LocalDate.of(2000, 5, 15), "421");
        String requestBody = "{\"name\":\"Дима Сыров\",\"dateOfBirth\":\"2000-05-15\",\"groupNumber\":\"421\"}";
        String responseBody = "{\"id\":1,\"name\":\"Дима Сыров\",\"dateOfBirth\":\"2000-05-15\",\"groupNumber\":\"421\"}";

        stubFor(post("/api/students")
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        StudentResponse result = studentService.addStudent(request);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Дима Сыров");
    }

    @Test
    @DisplayName("deleteStudent_ShouldThrowNotFoundException_WhenStudentDoesNotExist")
    void deleteStudent_ShouldThrowNotFoundException_WhenStudentDoesNotExist() {
        stubFor(delete("/api/students/99")
                .willReturn(WireMock.aResponse().withStatus(404)));

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with ID 99 not found");
    }
}
