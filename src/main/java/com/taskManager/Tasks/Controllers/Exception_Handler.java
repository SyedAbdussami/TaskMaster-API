package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Exception.CustomExceptionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Date;

@ControllerAdvice
public class Exception_Handler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( CustomException.class)
    protected ResponseEntity<?> handleConflict(CustomException ex){
        ModelMapper mapper=new ModelMapper();
        CustomExceptionResponse cus=mapper.map(ex,CustomExceptionResponse.class);
        cus.setDate(new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(cus, HttpStatus.BAD_REQUEST);
    }
}
