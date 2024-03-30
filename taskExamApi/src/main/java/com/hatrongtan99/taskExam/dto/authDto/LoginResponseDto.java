package com.hatrongtan99.taskExam.dto.authDto;

public record LoginResponseDto(
        String id,
        String fullname,
        String email,
        String token,
        Boolean isActive

){
}
