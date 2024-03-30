package com.hatrongtan99.taskExam.service.impl;

import com.hatrongtan99.taskExam.dto.ExamQuestionDto.DetailExamQuestionResponseDto;
import com.hatrongtan99.taskExam.dto.TakeExamDto.StudentAnswerRequestDto;
import com.hatrongtan99.taskExam.dto.TakeExamDto.TakeExamRequestDto;
import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicDetailResponse;
import com.hatrongtan99.taskExam.entity.*;
import com.hatrongtan99.taskExam.entity.enumeration.TakeExamStatus;
import com.hatrongtan99.taskExam.exception.BadRequestException;
import com.hatrongtan99.taskExam.exception.NotFoundException;
import com.hatrongtan99.taskExam.repository.StudentAnswerRepo;
import com.hatrongtan99.taskExam.repository.TakeExamRepo;
import com.hatrongtan99.taskExam.service.IExamTopicService;
import com.hatrongtan99.taskExam.service.IReadTakeExamService;
import com.hatrongtan99.taskExam.service.IStudentTakeExamService;
import com.hatrongtan99.taskExam.service.IUserService;
import com.hatrongtan99.taskExam.utils.CommonUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TakeExamService implements IStudentTakeExamService, IReadTakeExamService {

    private final TakeExamRepo takeExamRepo;
    private final StudentAnswerRepo studentAnswerRepo;
    private final IExamTopicService examTopicService;
    private final IUserService userService;
    @Getter
    private final ExamQuestionService examQuestionService;

    @Override
    @Transactional
    public TakeExamEntity startCreateTakeExam(String userId, String examTopicId) {
        ExamTopicEntity examTopic = this.examTopicService.findById(examTopicId);
        UserEntity user = this.userService.findById(userId);

        TakeExamEntity takeExam = this.takeExamRepo.findByStudentIdAndExamTopicIdAndStatus(user, examTopic, TakeExamStatus.PENDING).orElse(null);
        if (takeExam != null) {
            throw new BadRequestException("please submit previous before create new");
        }
        TakeExamEntity newTakeExam = TakeExamEntity.builder()
                .examTopicId(examTopic)
                .studentId(user)
                .status(TakeExamStatus.PENDING)
                .timeStart(ZonedDateTime.now())
                .timeExpires(examTopic.getTimeExpires())
                .questionNumber(examTopic.getQuestionNumber())
                .score(0f)
                .build();
        return this.takeExamRepo.save(newTakeExam);
    }

    @Override
    @Transactional
    public TakeExamEntity submitExam(String userId, String takeExamId, TakeExamRequestDto body) {
        TakeExamEntity takeExam = this.takeExamRepo.findById(takeExamId).orElseThrow(
                () -> new NotFoundException("Take exam not found")
        );
        if (takeExam.getStatus().equals(TakeExamStatus.SUBMITTED) || takeExam.getStatus().equals(TakeExamStatus.EXPIRES)) {
            throw new BadRequestException("Take Exam already submitted");
        }
        if (!takeExam.getStudentId().getId().equals(userId)) {
            throw new BadRequestException("Invalid user");
        }
        float score = 0;
        float scorePerQuestion = (float) 10 / takeExam.getQuestionNumber();
        boolean isTimeExpires = CommonUtils.validateTimeIsExpires(takeExam.getTimeExpires(), takeExam.getTimeStart(), ZonedDateTime.now());
        Set<StudentAnswerEntity> studentAnswer = new HashSet<>();
        for (StudentAnswerRequestDto answer : body.studentAnswer()) {
            ExamQuestionEntity examQuestion = this.examQuestionService.findById(answer.questionId());
            List<ChoiceEntity> answerOfQuestion = this.examQuestionService.getAllAnswerOfQuestionById(examQuestion.getId());

            StudentAnswerEntity answerEntity = StudentAnswerEntity.builder()
                    .takeExamId(takeExam)
                    .questionId(examQuestion)
                    .isActive(true)
                    .build();
            for (String id : answer.choicesId()) {
                ChoiceEntity choice = this.examQuestionService.findChoiceById(id);
                if (answerOfQuestion.contains(choice)) {
                    score += scorePerQuestion / answerOfQuestion.size();
                }
                answerEntity.getChoices().add(choice);
            }
            studentAnswer.add(answerEntity);
        }
        this.studentAnswerRepo.saveAll(studentAnswer);
        takeExam.setAnswer(studentAnswer);
        takeExam.setTimeSubmit(ZonedDateTime.now());
        if (isTimeExpires) {
            takeExam.setStatus(TakeExamStatus.EXPIRES);
            takeExam.setScore(0f);
        } else {
            takeExam.setStatus(TakeExamStatus.SUBMITTED);
            takeExam.setScore(score);
        }
        return this.takeExamRepo.saveAndFlush(takeExam);
    }

    @Override
    public ExamTopicDetailResponse getTakeExamQuestion(String takeExamId) {
        String topicId = this.takeExamRepo.findByIdAndJoinTopic(takeExamId)
                .orElseThrow(() -> new NotFoundException("Exam not found"));
        ExamTopicEntity topic = this.examTopicService.getDetail(topicId);
        List<DetailExamQuestionResponseDto> questions = new ArrayList<>();
        for (ExamQuestionEntity question : topic.getQuestions()) {
            questions.add(DetailExamQuestionResponseDto.mapToDto(question, false));
        }
        return new ExamTopicDetailResponse(
                topic.getId(),
                topic.getCode(),
                topic.getTitle(),
                questions,
                topic.getQuestionNumber(),
                topic.getTimeExpires().toMinutes(),
                topic.getIsActive()
        );
    }

    @Override
    public Page<TakeExamEntity> getListByTopic(String topicId, Map<String, String> allRequestParams, Pageable pageable) {

        if (allRequestParams.get("sort") != null) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), this.getSort(Sort.Direction.fromString(allRequestParams.get("sort")), "score"));
        }
        // validate if not exist throw
        this.examTopicService.findById(topicId);
        return this.takeExamRepo.findByExamTopicId(topicId, pageable);
    }

    @Override
    public Page<TakeExamEntity> getListByUserId(String userId, Pageable pageable) {
        this.userService.findById(userId);
        return this.takeExamRepo.findByStudentId(userId, pageable);
    }

    private Sort getSort(Sort.Direction direction, String... properties) {
        Sort sort = null;

        switch (direction) {
            case DESC:
                sort = Sort.by(Sort.Direction.DESC, properties);
                break;
            case ASC:
                sort = Sort.by(Sort.Direction.ASC, properties);
                break;
            default:
                sort = Sort.unsorted();
        }
        return sort;

    }

}
