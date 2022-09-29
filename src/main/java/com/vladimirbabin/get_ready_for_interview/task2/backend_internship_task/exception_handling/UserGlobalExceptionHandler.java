package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<EntityIncorrectData> handleException(
            RuntimeException exception) {
        EntityIncorrectData data = new EntityIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<EntityIncorrectData> handleException(
            NoSuchEntityException exception) {
        EntityIncorrectData data = new EntityIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
