package com.wasp203.core_service.service;

import com.wasp203.core_service.dto.CreateStudentRequest;
import com.wasp203.core_service.dto.UpdateStudentRequest;
import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.exception.NotFoundException;
import com.wasp203.core_service.mapper.StudentMapper;
import com.wasp203.core_service.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;


    @Test
    void getStudents_ShouldReturnListOfStudents() {
        // Arrange
        Student student = getStudent();
        when(studentRepository.findAll()).thenReturn(List.of(student));

        // Act
        List<Student> students = studentService.getStudents();

        // Assert
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0)).isEqualTo(student);

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudent_WhenStudentExists_ShouldReturnStudent() {
        // Arrange
        Student student = getStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Student foundStudent = studentService.getStudent(student.getId());

        // Assert
        assertThat(foundStudent).isNotNull()
                        .isEqualTo(student);

        verify(studentRepository, times(1)).findById(student.getId());
    }

    @Test
    void getStudent_WhenStudentDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudent(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");

        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void addStudent_ShouldSaveAndReturnStudent() {
        // Arrange
        Student student = getStudent();
        CreateStudentRequest createStudentRequest = getCreateStudentRequest();

        when(studentMapper.toEntity(createStudentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);

        Student savedStudent = studentService.addStudent(createStudentRequest);

        assertThat(savedStudent)
                .isNotNull()
                .isEqualTo(student);

        verify(studentMapper, times(1)).toEntity(createStudentRequest);
        verify(studentRepository, times(1)).save(student);
    }



    @Test
    void updateStudent_WhenStudentExists_ShouldUpdateAndReturnStudent() {
        // Arrange
        String name = "Ilya";
        Student student = getStudent();
        UpdateStudentRequest updateStudentRequest = new UpdateStudentRequest(
                student.getId(),
                name,
                null,
                null);
        Student expectedStudent = getStudent();
        expectedStudent.setName(name);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentMapper.partialUpdate(updateStudentRequest, student)).thenReturn(expectedStudent);
        when(studentRepository.save(expectedStudent)).thenReturn(expectedStudent);

        // Act
        Student updatedStudent = studentService.updateStudent(updateStudentRequest);

        // Assert
        assertThat(updatedStudent)
                .isNotNull()
                .isEqualTo(expectedStudent);

        verify(studentRepository, times(1)).findById(student.getId());
        verify(studentMapper, times(1)).partialUpdate(updateStudentRequest, student);
        verify(studentRepository, times(1)).save(updatedStudent);
    }

    @Test
    void updateStudent_WhenStudentDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        UpdateStudentRequest updateStudentRequest = new UpdateStudentRequest(
                1L,
                null,
                null,
                null);

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.updateStudent(updateStudentRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");

        verify(studentRepository, times(1)).findById(1L);
        verify(studentMapper, never()).partialUpdate(any(), any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_WhenStudentExists_ShouldDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStudent_WhenStudentDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> studentService.deleteStudent(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");

        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, never()).deleteById(1L);
    }

    private static Student getStudent() {
        return Student.builder()
                .id(1L)
                .name("Sima Dirov")
                .groupNumber("121")
                .dateOfBirth(LocalDate.now())
                .build();
    }

    private CreateStudentRequest getCreateStudentRequest() {
        Student student = getStudent();
        return new CreateStudentRequest(student.getName(), student.getDateOfBirth(), student.getGroupNumber());
    }
}