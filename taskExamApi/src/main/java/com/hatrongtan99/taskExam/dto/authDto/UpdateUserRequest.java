package com.hatrongtan99.taskExam.dto.authDto;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotNull
        String id,
        @NotNull
        String email,
        @NotNull
        String fullname,
        String password
) {
}
