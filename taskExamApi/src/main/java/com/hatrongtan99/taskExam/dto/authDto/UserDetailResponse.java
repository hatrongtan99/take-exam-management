package com.hatrongtan99.taskExam.dto.authDto;

import com.hatrongtan99.taskExam.entity.UserEntity;

public record UserDetailResponse(
        String id,
        String fullname,
        String email,
        String role,
        Boolean isActive
) {

    public static UserDetailResponse mapToDto(UserEntity entity) {
        return new UserDetailResponse(
                entity.getId(),
                entity.getFullname(),
                entity.getEmail(),
                entity.getAuthorities().getName(),
                entity.getIsActive()
        );
    }
}
