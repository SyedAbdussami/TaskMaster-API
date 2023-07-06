package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Exception.CustomExceptionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Date;

@ControllerAdvice
public class Exception_Handler extends ResponseEntityExceptionHandler {
    ModelMapper mapper=new ModelMapper();
    @ExceptionHandler( CustomException.class)
    protected ResponseEntity<?> handleConflict(CustomException ex){
        CustomExceptionResponse cus=mapper.map(ex,CustomExceptionResponse.class);
        cus.setDate(new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(cus, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleAnyException(Exception ex){
        CustomExceptionResponse customExceptionResponse=mapper.map(ex,CustomExceptionResponse.class);
        System.out.println(customExceptionResponse);
        customExceptionResponse.setDate(new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(customExceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);

    }


//    @ExceptionHandler(value = {NullPointerException.class})
//    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex){
//        String errorMessageDesc=ex.toString().split(":")[0];
//        if(errorMessageDesc==null){
//            errorMessageDesc=ex.toString();
//        }
//        ErrorMessage errorMessage=new ErrorMessage(new Date(),errorMessageDesc);
//        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(value = {UserServiceException.class})
//    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex){
//        String errorMessageDesc=ex.toString();
//        if(errorMessageDesc==null){
//            errorMessageDesc=ex.toString();
//        }
//        ErrorMessage errorMessage=new ErrorMessage(new Date(),errorMessageDesc);
//        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
}
