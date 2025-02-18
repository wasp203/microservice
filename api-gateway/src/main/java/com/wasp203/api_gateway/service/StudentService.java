package com.wasp203.api_gateway.service;

import com.wasp203.api_gateway.dto.CreateStudentRequest;
import com.wasp203.api_gateway.dto.StudentResponse;
import com.wasp203.api_gateway.dto.UpdateStudentRequest;
import com.wasp203.api_gateway.exception.CoreServiceException;
import com.wasp203.api_gateway.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final RestTemplate restTemplate;
    @Value("${core-service.url}/students")
    private String coreServiceStudentsApiUrl;

    public List<StudentResponse> getStudents() {
        try {
            ResponseEntity<StudentResponse[]> response = restTemplate.getForEntity(coreServiceStudentsApiUrl, StudentResponse[].class);
            return List.of(Objects.requireNonNull(response.getBody()));
        } catch (RestClientException e) {
            throw new CoreServiceException("Error when getting the list of students", e);
        }
    }

    public StudentResponse getStudent(Long id) {
        try {
            return restTemplate.getForObject(coreServiceStudentsApiUrl + "/" + id, StudentResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Student with ID " + id + " not found");
        } catch (RestClientException e) {
            throw new CoreServiceException("Error when receiving a student", e);
        }
    }

    public StudentResponse addStudent(CreateStudentRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateStudentRequest> entity = new HttpEntity<>(request, headers);
            return restTemplate.postForObject(coreServiceStudentsApiUrl, entity, StudentResponse.class);
        } catch (RestClientException e) {
            throw new CoreServiceException("Error when adding a student", e);
        }
    }

    public StudentResponse updateStudent(UpdateStudentRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UpdateStudentRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<StudentResponse> response = restTemplate.exchange(coreServiceStudentsApiUrl, HttpMethod.PUT, entity, StudentResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Student with ID " + request.id() + " not found");
        } catch (RestClientException e) {
            throw new CoreServiceException("Error when updating a student", e);
        }
    }

    public void deleteStudent(Long id) {
        try {
            restTemplate.delete(coreServiceStudentsApiUrl + "/" + id);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Student with ID " + id + " not found");
        } catch (RestClientException e) {
            throw new CoreServiceException("Error when deleting a student", e);
        }
    }
}
