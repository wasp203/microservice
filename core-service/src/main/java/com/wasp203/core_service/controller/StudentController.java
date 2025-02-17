package com.wasp203.core_service.controller;

import com.wasp203.core_service.dto.CreateStudentRequest;
import com.wasp203.core_service.dto.UpdateStudentRequest;
import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public Student addStudent(@RequestBody CreateStudentRequest request) {
        return studentService.addStudent(request);
    }

    @PutMapping
    public Student updateStudent(@RequestBody UpdateStudentRequest request) {
        return studentService.updateStudent(request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
