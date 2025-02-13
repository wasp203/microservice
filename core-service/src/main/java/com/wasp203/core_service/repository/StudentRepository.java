package com.wasp203.core_service.repository;

import com.wasp203.core_service.entity.Student;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface StudentRepository extends JpaRepository<Student, Long> {
}
