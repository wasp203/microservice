package com.wasp203.api_gateway.controller;

import com.wasp203.api_gateway.dto.CreateStudentRequest;
import com.wasp203.api_gateway.dto.StudentResponse;
import com.wasp203.api_gateway.dto.UpdateStudentRequest;
import com.wasp203.api_gateway.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Студенты", description = "Эндпоинты для работы со студентами")
public class StudentController {

    @Operation(summary = "Получить список студентов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список студентов успешно получен"),
            @ApiResponse(responseCode = "502", description = "Ошибка внешнего сервиса"),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentResponse> getStudents() {
        return studentService.getStudents();
    }

    private final StudentService studentService;


    @Operation(summary = "Получить студента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студент найден"),
            @ApiResponse(responseCode = "404", description = "Студент не найден"),
            @ApiResponse(responseCode = "502", description = "Ошибка внешнего сервиса")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @Operation(summary = "Добавить нового студента")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Студент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "502", description = "Ошибка внешнего сервиса")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse addStudent(@RequestBody CreateStudentRequest request) {
        return studentService.addStudent(request);
    }

    @Operation(summary = "Обновить данные студента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные студента обновлены"),
            @ApiResponse(responseCode = "404", description = "Студент не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "502", description = "Ошибка внешнего сервиса")
    })
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public StudentResponse updateStudent(@RequestBody UpdateStudentRequest request) {
        return studentService.updateStudent(request);
    }

    @Operation(summary = "Удалить студента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Студент удален"),
            @ApiResponse(responseCode = "404", description = "Студент не найден"),
            @ApiResponse(responseCode = "502", description = "Ошибка внешнего сервиса")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
