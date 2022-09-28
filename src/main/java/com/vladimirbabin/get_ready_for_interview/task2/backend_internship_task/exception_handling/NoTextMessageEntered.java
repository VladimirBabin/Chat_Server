package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling;

public class NoTextMessageEntered extends RuntimeException {
    public NoTextMessageEntered(String message) {
        super(message);
    }
}
