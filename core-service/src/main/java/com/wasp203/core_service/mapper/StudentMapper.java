package com.wasp203.core_service.mapper;

import com.wasp203.core_service.dto.CreateStudentRequest;
import com.wasp203.core_service.entity.Student;
import com.wasp203.core_service.dto.UpdateStudentRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {
    Student toEntity(CreateStudentRequest createStudentRequest);

    CreateStudentRequest toCreateStudentDto(Student student);

    Student toEntity(UpdateStudentRequest updateStudentRequest);

    UpdateStudentRequest toUpdateStudentRequest(Student student);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Student partialUpdate(UpdateStudentRequest studentRequest, @MappingTarget Student user);
}
