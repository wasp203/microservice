package com.wasp203.core_service.repository;

import com.wasp203.core_service.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("save should save student when valid object is provided")
    void save_ShouldSaveStudent_WhenValidObjectIsProvided() {
        // Arrange
        Student student = Student.builder()
                .name("Иван Иванов")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .groupNumber("121")
                .build();

        // Act
        Student savedStudent = studentRepository.save(student);

        // Assert
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("Иван Иванов");
    }

    @Test
    @DisplayName("findById should return student when student exists")
    void findById_ShouldReturnStudent_WhenStudentExists() {
        // Arrange
        Student student = studentRepository.save(Student.builder()
                .name("Петр Петров")
                .dateOfBirth(LocalDate.of(1999, 5, 15))
                .groupNumber("441")
                .build());

        // Act
        Optional<Student> foundStudent = studentRepository.findById(student.getId());

        // Assert
        assertThat(foundStudent.isPresent()).isTrue();
        assertThat(foundStudent.orElseThrow()
                .getId()).isEqualTo(student.getId());
        assertThat(foundStudent.get().getName()).isEqualTo("Петр Петров");
    }

    @Test
    @DisplayName("deleteById should delete student when student exists")
    void deleteById_ShouldDeleteStudent_WhenStudentExists() {
        // Arrange
        Student student = studentRepository.save(Student.builder()
                .name("Сидор Сидоров")
                .dateOfBirth(LocalDate.of(2001, 3, 20))
                .groupNumber("321")
                .build());

        // Act
        studentRepository.deleteById(student.getId());
        Optional<Student> deletedStudent = studentRepository.findById(student.getId());

        // Assert
        assertThat(deletedStudent).isEmpty();
    }
}
