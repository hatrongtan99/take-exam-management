package com.hatrongtan99.taskExam.dto.TakeExamDto;

import java.util.Set;

public record StudentAnswerRequestDto(
        String questionId,
        Set<String> choicesId
) {
}
