package com.hatrongtan99.taskExam.dto.authDto;

public record NewUserResponseDto(
        String id,
        String fullname,
        String email,
        String role,
        String password,
        Boolean isActive
) {
}
