package com.wasp203.core_service.service;

import com.wasp203.core_service.dto.CreateStudentRequest;
import com.wasp203.core_service.dto.UpdateStudentRequest;
import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.exception.NotFoundException;
import com.wasp203.core_service.mapper.StudentMapper;
import com.wasp203.core_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Student with id " + id + " not found"));
    }

    public Student addStudent(CreateStudentRequest request) {
        return studentRepository.save(studentMapper.toEntity(request));
    }

    public Student updateStudent(UpdateStudentRequest request) {
        Student student = studentRepository.findById(request.id()).orElseThrow(
                () -> new NotFoundException("Student with id " + request.id() + " not found"));
        return studentRepository.save(studentMapper.partialUpdate(request, student));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id))
            throw new NotFoundException("Student with id " + id + " not found");

        studentRepository.deleteById(id);
    }
}
