package com.hatrongtan99.taskExam.dto.examTopicDto;

import com.hatrongtan99.taskExam.entity.ExamTopicEntity;

public record ExamTopicResponseDto(
        String id,
        String code,
        String title,
        Integer questionNumber,
        Long timeExpires,
        Boolean isActive
) {

    public static ExamTopicResponseDto mapToDto(ExamTopicEntity entity) {
        return new ExamTopicResponseDto(
                entity.getId(),
                entity.getCode(),
                entity.getTitle(),
                entity.getQuestionNumber(),
                entity.getTimeExpires().toMinutes(),
                entity.getIsActive()
        );
    }
}
