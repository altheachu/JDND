package com.example.demo.utils;

import com.example.demo.utils.enums.BusinessExceptionType;

import java.util.HashMap;
import java.util.Map;

public class ExceptionResponse {

    private BusinessExceptionType type;
    private Map<String, Object> info = new HashMap<>();

    public static ExceptionResponse of(BusinessExceptionType type, Map<String, Object> info) {
        ExceptionResponse res = new ExceptionResponse();
        res.type = type;
        res.info = info;
        return res;
    }

    public BusinessExceptionType getType() {
        return type;
    }

    public void setType(BusinessExceptionType type) {
        this.type = type;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
