package com.hatrongtan99.taskExam.dto.TakeExamDto;

import com.hatrongtan99.taskExam.entity.TakeExamEntity;
import com.hatrongtan99.taskExam.entity.enumeration.TakeExamStatus;

import java.time.ZonedDateTime;

record StudentInfoResponse(
        String studentId,
        String fullname,
        String email
) {

}

public record TakeExamResponseDto(
        String id,
        StudentInfoResponse student,
        String examTopicId,
        TakeExamStatus status,
        ZonedDateTime timeStart,
        ZonedDateTime timeSubmit,
        Float score,
        Boolean isActive
) {

    public static TakeExamResponseDto mapToDto(TakeExamEntity entity) {
        StudentInfoResponse student = new StudentInfoResponse(entity.getStudentId().getId(), entity.getStudentId().getFullname(), entity.getStudentId().getEmail());
        return new TakeExamResponseDto(
                entity.getId(),
                student,
                entity.getExamTopicId().getId(),
                entity.getStatus(),
                entity.getTimeStart(),
                entity.getTimeSubmit(),
                entity.getScore(),
                entity.getIsActive()
        );
    }
}
