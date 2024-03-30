package com.hatrongtan99.taskExam.dto.authDto;

import jakarta.validation.constraints.NotNull;

public record NewUserRequestDto(
    @NotNull
    String email,
    @NotNull
    String fullname
) {
}
