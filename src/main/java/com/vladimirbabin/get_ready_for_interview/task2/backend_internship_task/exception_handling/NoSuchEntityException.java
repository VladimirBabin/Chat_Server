package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling;

public class NoSuchEntityException extends RuntimeException{
    public NoSuchEntityException(String message) {
        super(message);
    }
}
