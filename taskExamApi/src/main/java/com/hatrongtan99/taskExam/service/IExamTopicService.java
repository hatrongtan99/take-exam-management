package com.hatrongtan99.taskExam.service;

import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicSaveRequestDto;
import com.hatrongtan99.taskExam.entity.ExamTopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IExamTopicService {
    Page<ExamTopicEntity> getList(Pageable pageable);
    ExamTopicEntity getDetail(String id);
    ExamTopicEntity findById(String id);
    ExamTopicEntity createNew(ExamTopicSaveRequestDto body);
    void removeById(String id);
    ExamTopicEntity update(String id, ExamTopicSaveRequestDto body);
}
