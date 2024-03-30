package com.hatrongtan99.taskExam.service.impl;

import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicSaveRequestDto;
import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;
import com.hatrongtan99.taskExam.entity.ExamTopicEntity;
import com.hatrongtan99.taskExam.exception.ConflictException;
import com.hatrongtan99.taskExam.exception.NotFoundException;
import com.hatrongtan99.taskExam.repository.ExamTopicRepo;
import com.hatrongtan99.taskExam.service.IExamTopicService;
import com.hatrongtan99.taskExam.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamTopicService implements IExamTopicService {
    private final ExamQuestionService examQuestionService;
    private final ExamTopicRepo examTopicRepo;

    @Override
    public Page<ExamTopicEntity> getList(Pageable pageable) {
        return this.examTopicRepo.findAll(pageable);
    }

    @Override
    public ExamTopicEntity getDetail(String id) {
        return this.examTopicRepo.findByIdAndJoinQuestionAndChoices(id).orElseThrow(
                () -> new NotFoundException("Exam topic not found")
        );
    }

    @Override
    public ExamTopicEntity findById(String id) {
        return this.examTopicRepo.findByIdAndIsActiveIsTrue(id).orElseThrow(
                () -> new NotFoundException("Topic not found")
        );
    }

    @Override
    @Transactional
    public ExamTopicEntity createNew(ExamTopicSaveRequestDto body) {
        ExamTopicEntity newExamTopic = ExamTopicEntity.builder()
                .title(body.title())
                .isActive(true)
                .build();
        String code = this.validateAndGetCode(body.code());
        newExamTopic.setCode(code);
        for (String questionId : body.questionId()) {
            ExamQuestionEntity question = this.examQuestionService.findById(questionId);
            newExamTopic.addQuestion(question);
        }
        newExamTopic.setTimeExpires(Duration.ofMinutes(body.timeExpires()));
        newExamTopic.setQuestionNumber(newExamTopic.getQuestions().size());
        return this.examTopicRepo.save(newExamTopic);
    }

    @Override
    public void removeById(String id) {
        ExamTopicEntity examTopic = this.findById(id);
        examTopic.setIsActive(false);
        this.examTopicRepo.save(examTopic);
    }

    @Override
    @Transactional
    public ExamTopicEntity update(String id, ExamTopicSaveRequestDto body) {
        ExamTopicEntity examTopic = this.getDetail(id);
        if (body.code() != null) {
            String code = this.validateAndGetCode(body.code());
            examTopic.setCode(code);
        }
        examTopic.setTitle(body.title());
        examTopic.setTimeExpires(Duration.ofMinutes(body.timeExpires()));
        Set<String> oldQuestionId = examTopic.getQuestions().stream().map(ExamQuestionEntity::getId).collect(Collectors.toSet());
        // list question remove
        Set<ExamQuestionEntity> removeQuestion = examTopic.getQuestions().stream()
                .filter(question -> !body.questionId().contains(question.getId()))
                .collect(Collectors.toSet());
        examTopic.removeQuestion(removeQuestion);
        for (String questionId : body.questionId()) {
            // add new question
            if (!oldQuestionId.contains(questionId)) {
                ExamQuestionEntity newQuestion = this.examQuestionService.findById(questionId);
                examTopic.addQuestion(newQuestion);
            }
        }
        // update question number
        examTopic.setQuestionNumber(examTopic.getQuestions().size());
        return this.examTopicRepo.saveAndFlush(examTopic);
    }

    private String validateAndGetCode(String code) {
        if (code == null) {
            code = CommonUtils.generateCode();
            while (this.examTopicRepo.existsByCode(code)) {
                code = CommonUtils.generateCode();
            }
        } else {
            if (this.examTopicRepo.existsByCode(code)) {
                throw new ConflictException("Code: '" + code + "' already exist");
            }
        }
        return code;
    }
}
