package com.taskManager.Tasks.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomException extends RuntimeException{

    String errorMessage;
    String nextSteps;
    HttpStatus httpStatus;

}
