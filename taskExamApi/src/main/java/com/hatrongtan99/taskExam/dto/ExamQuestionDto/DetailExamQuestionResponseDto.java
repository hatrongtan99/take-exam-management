package com.hatrongtan99.taskExam.dto.ExamQuestionDto;

import com.hatrongtan99.taskExam.dto.choiceDto.ChoiceResponseDto;
import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;

import java.util.List;

public record DetailExamQuestionResponseDto(
    String id,
    String title,
    List<ChoiceResponseDto> choices,
    Boolean isActive
) {
    public static DetailExamQuestionResponseDto mapToDto(ExamQuestionEntity entity, Boolean seeAnswer) {
        return new DetailExamQuestionResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getChoices().stream().map(choice -> new ChoiceResponseDto(
                        choice.getId(),
                        choice.getTitle(),
                        seeAnswer ? choice.getIsAnswer(): null
                )).toList(),
                entity.getIsActive()
        );
    }
}
