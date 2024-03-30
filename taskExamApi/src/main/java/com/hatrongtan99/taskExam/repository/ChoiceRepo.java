package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.ChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChoiceRepo extends JpaRepository<ChoiceEntity, String> {

    @Query(value = """
            FROM ChoiceEntity AS c LEFT JOIN c.questionId AS q WHERE q.id = :questionId AND c.isAnswer = true
            """)
    List<ChoiceEntity> findAnswerOfQuestion(@Param("questionId") String questionId);
}
