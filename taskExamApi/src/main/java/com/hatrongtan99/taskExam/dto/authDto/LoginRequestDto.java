package com.hatrongtan99.taskExam.dto.authDto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
