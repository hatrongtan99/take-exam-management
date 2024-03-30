package com.hatrongtan99.taskExam.dto;

import java.util.List;

public record PageableResponseDto<T>(
       List<T> records,
       MetadataResponseDto _metadata
) {
}
