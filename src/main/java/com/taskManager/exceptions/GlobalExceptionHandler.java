package com.taskManager.exceptions;

import com.taskManager.exceptions.response.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleTaskNotFoundException(TaskNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Task not found", ex.getMessage());
    }

    public ResponseEntity<ErrorMessage> buildErrorResponse(HttpStatus status, String error, String message) {
        ErrorMessage errorMessage = new ErrorMessage(error, message);
        return ResponseEntity.status(status).body(errorMessage);
    }
}
