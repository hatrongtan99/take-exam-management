package com.hatrongtan99.taskExam.service;

import com.hatrongtan99.taskExam.dto.ExamQuestionDto.ExamQuestionSaveDto;
import com.hatrongtan99.taskExam.entity.ChoiceEntity;
import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IExamQuestionService {

    Page<ExamQuestionEntity> getList(Pageable pageable);
    ExamQuestionEntity getDetailById(String id);
    ExamQuestionEntity findById(String id);
    ExamQuestionEntity createNew(ExamQuestionSaveDto body);
    ExamQuestionEntity update(String id, ExamQuestionSaveDto body);
    void deleteById(String id);
    List<ChoiceEntity> getAllAnswerOfQuestionById(String id);
    ChoiceEntity findChoiceById(String id);
}
