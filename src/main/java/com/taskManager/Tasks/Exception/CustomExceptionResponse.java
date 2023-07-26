package com.taskManager.Tasks.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomExceptionResponse extends Exception{
    String message;
    String nextSteps;
    HttpStatus httpStatus;
    Date date;
}
