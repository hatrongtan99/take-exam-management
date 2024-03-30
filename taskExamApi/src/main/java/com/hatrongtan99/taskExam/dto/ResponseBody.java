package com.hatrongtan99.taskExam.dto;

import org.springframework.http.HttpStatus;

public record ResponseBody(
        HttpStatus status,
        String message
) {
}
