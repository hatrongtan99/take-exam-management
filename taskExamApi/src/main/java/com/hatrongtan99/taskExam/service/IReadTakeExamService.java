package com.hatrongtan99.taskExam.service;

import com.hatrongtan99.taskExam.entity.TakeExamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface IReadTakeExamService {
    Page<TakeExamEntity> getListByTopic(String topicId, Map<String, String> allRequestParams, Pageable pageable);
    Page<TakeExamEntity> getListByUserId(String userId, Pageable pageable);

}
