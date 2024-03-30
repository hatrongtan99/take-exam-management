package com.hatrongtan99.taskExam.dto.examTopicDto;

import com.hatrongtan99.taskExam.dto.ExamQuestionDto.DetailExamQuestionResponseDto;

import java.util.List;

public record ExamTopicDetailResponse(
        String id,
        String code,
        String title,
        List<DetailExamQuestionResponseDto> questions,
        Integer questionNumber,
        Long timeExpires,
        Boolean isActive
) {
}
