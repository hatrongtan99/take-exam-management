package com.hatrongtan99.taskExam.service.impl;

import com.hatrongtan99.taskExam.dto.ExamQuestionDto.ExamQuestionSaveDto;
import com.hatrongtan99.taskExam.dto.choiceDto.ChoiceSaveDto;
import com.hatrongtan99.taskExam.entity.ChoiceEntity;
import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;
import com.hatrongtan99.taskExam.exception.BadRequestException;
import com.hatrongtan99.taskExam.exception.NotFoundException;
import com.hatrongtan99.taskExam.repository.ChoiceRepo;
import com.hatrongtan99.taskExam.repository.ExamQuestionRepo;
import com.hatrongtan99.taskExam.service.IExamQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamQuestionService implements IExamQuestionService {
    private final ExamQuestionRepo examQuestionRepo;
    private final ChoiceRepo choiceRepo;

    @Override
    public Page<ExamQuestionEntity> getList(Pageable pageable) {
        return this.examQuestionRepo.findAll(pageable);
    }

    @Override
    public ExamQuestionEntity getDetailById(String id) {
        return this.examQuestionRepo.findByIdAndJoinChoices(id).orElseThrow(
                () -> new NotFoundException("Question not found")
        );
    }

    @Override
    public ExamQuestionEntity findById(String id) {
        return this.examQuestionRepo.findByIdAndIsActiveIsTrue(id).orElseThrow(
                () -> new NotFoundException("Question not found")
        );
    }

    @Override
    @Transactional
    public ExamQuestionEntity createNew(ExamQuestionSaveDto body) {
        ExamQuestionEntity newQuestion = ExamQuestionEntity.builder()
                .title(body.title())
                .isActive(true)
                .build();

        setChoices(body.choices(), newQuestion);
        return this.examQuestionRepo.saveAndFlush(newQuestion);
    }

    @Override
    @Transactional
    public ExamQuestionEntity update(String id, ExamQuestionSaveDto body) {
        ExamQuestionEntity question = this.getDetailById(id);
        question.setTitle(body.title());
        setChoices(body.choices(), question);
        return question;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        ExamQuestionEntity question = this.findById(id);
        question.setIsActive(false);
    }

    @Override
    public List<ChoiceEntity> getAllAnswerOfQuestionById(String id) {
        return this.choiceRepo.findAnswerOfQuestion(id);
    }

    @Override
    public ChoiceEntity findChoiceById(String id) {
        return this.choiceRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Choice not found")
        );
    }

    private void setChoices(List<ChoiceSaveDto> choices, ExamQuestionEntity question) {
        boolean haveAnswer = false;
        List<ChoiceEntity> newChoice = new ArrayList<>();
        for (ChoiceSaveDto choice : choices) {
            if (choice.isAnswer()) {
                haveAnswer = true;
            }
            newChoice.add(ChoiceEntity.builder()
                    .title(choice.title())
                    .isActive(true)
                    .isAnswer(choice.isAnswer())
                    .questionId(question)
                    .build()
            );
        }
        if (!haveAnswer) {
            throw new BadRequestException("The question must have at least one answer");
        }
        // clear old choices if exist
        if (!question.getChoices().isEmpty()) {
            question.getChoices().clear();
        }
        question.getChoices().addAll(newChoice);
    }


}
