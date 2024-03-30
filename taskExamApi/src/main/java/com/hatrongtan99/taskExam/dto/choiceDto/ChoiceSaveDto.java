package com.hatrongtan99.taskExam.dto.choiceDto;

import jakarta.validation.constraints.NotNull;

public record ChoiceSaveDto(
    @NotNull
    String title,
    @NotNull
    Boolean isAnswer
) {
}
