package com.wasp203.core_service.service;

import com.wasp203.core_service.dto.CreateStudentRequest;
import com.wasp203.core_service.dto.UpdateStudentRequest;
import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.exception.NotFoundException;
import com.wasp203.core_service.mapper.StudentMapper;
import com.wasp203.core_service.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("getStudents_ReturnsListOfStudents_WhenDataExists")
    void getStudents_ReturnsListOfStudents_WhenDataExists() {
        // Arrange
        List<Student> students = List.of(new Student(), new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<Student> result = studentService.getStudents();

        // Assert
        assertThat(result).hasSize(3).containsExactlyElementsOf(students);
        verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("getStudent_ReturnsStudent_WhenExistsInDatabase")
    void getStudent_ReturnsStudent_WhenExistsInDatabase() {
        // Arrange
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Student result = studentService.getStudent(1L);

        // Assert
        assertThat(result).isEqualTo(student);
        verify(studentRepository).findById(1L);
    }

    @Test
    @DisplayName("getStudent_ThrowsException_WhenStudentNotFound")
    void getStudent_ThrowsException_WhenStudentNotFound() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudent(studentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Student with id " + studentId + " not found");

        verify(studentRepository).findById(studentId);
    }

    @Test
    @DisplayName("addStudent_SavesStudent_WhenDataIsValid")
    void addStudent_SavesStudent_WhenDataIsValid() {
        // Arrange
        CreateStudentRequest request = mock(CreateStudentRequest.class);
        Student student = mock(Student.class);
        when(studentMapper.toEntity(request)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);

        // Act
        Student result = studentService.addStudent(request);

        // Assert
        assertThat(result).isEqualTo(student);
        verify(studentMapper).toEntity(request);
        verify(studentRepository).save(student);
    }

    @Test
    @DisplayName("updateStudent_UpdatesStudent_WhenStudentExists")
    void updateStudent_UpdatesStudent_WhenStudentExists() {
        // Arrange
        UpdateStudentRequest request = mock(UpdateStudentRequest.class);
        Student existingStudent = mock(Student.class);
        Student updatedStudent = mock(Student.class);
        when(studentRepository.findById(request.id())).thenReturn(Optional.of(existingStudent));
        when(studentMapper.partialUpdate(request, existingStudent)).thenReturn(updatedStudent);
        when(studentRepository.save(updatedStudent)).thenReturn(updatedStudent);

        // Act
        Student result = studentService.updateStudent(request);

        // Assert
        assertThat(result).isEqualTo(updatedStudent);
        verify(studentRepository).findById(request.id());
        verify(studentMapper).partialUpdate(request, existingStudent);
        verify(studentRepository).save(updatedStudent);
    }

    @Test
    @DisplayName("updateStudent_ThrowsException_WhenStudentNotFound")
    void updateStudent_ThrowsException_WhenStudentNotFound() {
        // Arrange
        UpdateStudentRequest request = mock(UpdateStudentRequest.class);
        when(studentRepository.findById(request.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.updateStudent(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Student with id " + request.id() + " not found");

        verify(studentRepository).findById(request.id());
    }

    @Test
    @DisplayName("deleteStudent_DeletesStudent_WhenStudentExists")
    void deleteStudent_DeletesStudent_WhenStudentExists() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(true);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository).existsById(studentId);
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    @DisplayName("deleteStudent_ThrowsException_WhenStudentNotFound")
    void deleteStudent_ThrowsException_WhenStudentNotFound() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> studentService.deleteStudent(studentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Student with id " + studentId + " not found");

        verify(studentRepository).existsById(studentId);
    }
}
