package com.osttra.alpine.advices;

import com.osttra.alpine.exceptions.ProcessNotFoundException;
import com.osttra.alpine.exceptions.TaskDefinitionNotFound;
import com.osttra.alpine.exceptions.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProcessNotFoundException.class)
    public ResponseEntity<ApiError> handleProcessNotFoundException(ProcessNotFoundException ex)
    {
        ApiError apiError = ApiError.builder().httpStatus(HttpStatus.NOT_FOUND).error(ex.getLocalizedMessage())
                .timeStamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleTaskNotFoundException(TaskNotFoundException ex)
    {
        ApiError apiError = ApiError.builder().httpStatus(HttpStatus.NOT_FOUND).error(ex.getLocalizedMessage())
                .timeStamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskDefinitionNotFound.class)
    public ResponseEntity<ApiError> handleTaskDefinitionException(TaskDefinitionNotFound taskDefinitionNotFound)
    {
        ApiError apiError = ApiError.builder().httpStatus(HttpStatus.NOT_FOUND)
                .error(taskDefinitionNotFound.getLocalizedMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException runtimeException)
    {
            ApiError apiError = ApiError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .error(runtimeException.getLocalizedMessage())
                    .timeStamp(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
