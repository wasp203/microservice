package com.wasp203.core_service.service;

import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.exception.NotFoundException;
import com.wasp203.core_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public Student getStudent(Long id){
        return studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Student with id " + id + " not found"));
    }

    public Student addStudent(Student student){
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student){
        if(!studentRepository.existsById(student.getId()))
            throw new NotFoundException("Student with id " + student.getId() + " not found");

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id){
        if(!studentRepository.existsById(id))
            throw new NotFoundException("Student with id " + id + " not found");

        studentRepository.deleteById(id);
    }
}
