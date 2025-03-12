package com.wasp203.core_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wasp203.core_service.dto.CreateStudentRequest;
import com.wasp203.core_service.dto.UpdateStudentRequest;
import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    @DisplayName("getStudents_ShouldReturnListOfStudents_WhenDataExists")
    void getStudents_ShouldReturnListOfStudents_WhenDataExists() throws Exception {
        List<Student> students = List.of(new Student(1L, "Дима Сыров", LocalDate.of(2000, 5, 15), "421"));
        when(studentService.getStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    @DisplayName("getStudent_ShouldReturnStudent_WhenItExists")
    void getStudent_ShouldReturnStudent_WhenItExists() throws Exception {
        Student student = new Student(1L, "Дима Сыров", LocalDate.of(2000, 5, 15), "421");
        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @Test
    @DisplayName("addStudent_ShouldAddStudent_WhenDataIsValid")
    void addStudent_ShouldAddStudent_WhenDataIsValid() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest("Дима Сыров", LocalDate.of(2000, 5, 15), "421");
        Student student = new Student(1L, "Дима Сыров", LocalDate.of(2000, 5, 15), "421");
        when(studentService.addStudent(any(CreateStudentRequest.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @Test
    @DisplayName("updateStudent_ShouldUpdateStudent_WhenDataIsValid")
    void updateStudent_ShouldUpdateStudent_WhenDataIsValid() throws Exception {
        UpdateStudentRequest request = new UpdateStudentRequest(1L, "Дима Сыров", LocalDate.of(2000, 5, 15), "121");
        Student updatedStudent = new Student(1L, "Дима Сыров", LocalDate.of(2000, 5, 15), "121");
        when(studentService.updateStudent(any(UpdateStudentRequest.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedStudent)));
    }

    @Test
    @DisplayName("deleteStudent_ShouldDeleteStudent_WhenItExists")
    void deleteStudent_ShouldDeleteStudent_WhenItExists() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
