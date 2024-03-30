package com.hatrongtan99.taskExam.dto;

import org.springframework.data.domain.Page;

public record MetadataResponseDto(
        int page,
        int pageSize,
        int pageCount,
        int totalCount
) {
    public static <T>MetadataResponseDto mapToDto(Page<T> page) {
        return new MetadataResponseDto(
                page.getNumber(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                (int) page.getTotalElements()
        );
    }
}
