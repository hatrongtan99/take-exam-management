package com.hatrongtan99.taskExam.service;

import com.hatrongtan99.taskExam.dto.TakeExamDto.TakeExamRequestDto;
import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicDetailResponse;
import com.hatrongtan99.taskExam.entity.ExamTopicEntity;
import com.hatrongtan99.taskExam.entity.TakeExamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface IStudentTakeExamService {
    TakeExamEntity startCreateTakeExam(String userId, String examTopicId);
    TakeExamEntity submitExam(String userId, String takeExamId, TakeExamRequestDto body);
    ExamTopicDetailResponse getTakeExamQuestion(String takeExamId);

}
