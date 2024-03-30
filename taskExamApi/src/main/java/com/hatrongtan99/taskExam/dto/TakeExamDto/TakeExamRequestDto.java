package com.hatrongtan99.taskExam.dto.TakeExamDto;

import java.util.List;

public record TakeExamRequestDto(
    String examTopicId,
    List<StudentAnswerRequestDto> studentAnswer
) {
}
