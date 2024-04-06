package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, Long> {
}
