package com.hatrongtan99.taskExam.dto.ExamQuestionDto;

import com.hatrongtan99.taskExam.dto.choiceDto.ChoiceSaveDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExamQuestionSaveDto(
        @NotNull
        String title,
        List<ChoiceSaveDto> choices
) {
}
