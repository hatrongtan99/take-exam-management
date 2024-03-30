package com.hatrongtan99.taskExam.dto.authDto;

import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequestDto(
        @NotNull
        String email,
        @NotNull
        String roleName
) {
}
