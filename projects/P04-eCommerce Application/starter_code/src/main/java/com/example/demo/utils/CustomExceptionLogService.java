package com.example.demo.utils;

import com.example.demo.model.persistence.ExceptionLog;
import com.example.demo.model.persistence.repositories.ExceptionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomExceptionLogService {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    public ResponseEntity<ExceptionLog> create(ExceptionLog exceptionLog){
        exceptionLogRepository.save(exceptionLog);
        return ResponseEntity.ok(exceptionLog);
    }

}
