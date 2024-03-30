package com.hatrongtan99.taskExam.dto.ExamQuestionDto;

import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;

public record ExamQuestionResponseDto(
        String id,
        String title,
        Boolean isActive
) {
    public static ExamQuestionResponseDto mapToDto(ExamQuestionEntity entity) {
        return new ExamQuestionResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getIsActive()
        );
    }
}
