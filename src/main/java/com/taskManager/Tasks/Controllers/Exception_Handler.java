package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Exception.CustomExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.sql.Date;
import java.util.Arrays;

@ControllerAdvice
public class Exception_Handler extends ResponseEntityExceptionHandler {
    ModelMapper mapper=new ModelMapper();
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleConflict(CustomException ex){
        CustomExceptionResponse cus=mapper.map(ex,CustomExceptionResponse.class);
        cus.setDate(new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(cus, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AuthenticationException.class)
    protected  ResponseEntity<?> handleAuthenticationException(AuthenticationException ex){
        CustomExceptionResponse exceptionResponse=mapper.map(ex,CustomExceptionResponse.class);
        exceptionResponse.setMessage(ex.getExplanation());
//        exceptionResponse.setNextSteps(ex.getMessage());
        exceptionResponse.setDate(new Date(System.currentTimeMillis()));
        exceptionResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        System.out.println(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected  ResponseEntity<?> handleAuthorisationException(AccessDeniedException ex){
        CustomExceptionResponse exceptionResponse=mapper.map(ex,CustomExceptionResponse.class);
//        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDate(new Date(System.currentTimeMillis()));
        exceptionResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        System.out.println(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<?> handleJwTExpiredException(ExpiredJwtException ex){
        CustomExceptionResponse exceptionResponse=mapper.map(ex,CustomExceptionResponse.class);
        exceptionResponse.setDate(new Date(System.currentTimeMillis()));
        exceptionResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        System.out.println(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleAnyException(Exception ex){
        CustomExceptionResponse customExceptionResponse=mapper.map(ex,CustomExceptionResponse.class);
        System.out.println(ex.getMessage());
        System.out.println(customExceptionResponse.getMessage());
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
}
