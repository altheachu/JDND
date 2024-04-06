package com.example.demo.advice;


import com.example.demo.editor.LowercaseTextEditor;
import com.example.demo.exception.OperationAbsentItemsException;
import com.example.demo.model.persistence.ExceptionLog;
import com.example.demo.utils.CustomExceptionLogService;
import com.example.demo.utils.ExceptionResponse;
import com.example.demo.utils.enums.BusinessExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages="com.example.demo.controllers")
public class GeneralAdvice {

    @Autowired
    private CustomExceptionLogService customExceptionLogService;

    @ExceptionHandler(OperationAbsentItemsException.class)
    public ResponseEntity<ExceptionResponse> handleOperateAbsentItem(OperationAbsentItemsException e){
        // it is suitable to write down exception log in advice
        ExceptionLog eLog = new ExceptionLog();
        eLog.setApi(e.getApi());
        eLog.setMsg("absentItems:" + e.getItemIds().toString());
        customExceptionLogService.create(eLog);

        Map<String, Object> info = new HashMap<>();
        info.put("itemIds", e.getItemIds());

        ExceptionResponse res = new ExceptionResponse();
        res.setType(BusinessExceptionType.OPERATE_ABSENT_ITEM);
        res.setInfo(info);
        return ResponseEntity.unprocessableEntity().body(res);
    }

    // SpringBoot will process InitBinder first to amend request parameters and next process the controller method.
    @InitBinder({"username"})
    public void initUsername(WebDataBinder binder){
        binder.registerCustomEditor(String.class, new LowercaseTextEditor());
    }

}
