package com.hatrongtan99.taskExam.dto.examTopicDto;

import com.hatrongtan99.taskExam.validation.ValidateTimeExpires;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExamTopicSaveRequestDto(
        @NotNull
        @NotBlank
        String title,
        @Min(value = 4, message = "Code must have min 4 length")
        String code,
        @Min(value = 1, message = "time not negative, min equal 1 is required")
        @ValidateTimeExpires
        Integer timeExpires,

        List<String> questionId
) {
}
