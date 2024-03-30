package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.ExamTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExamTopicRepo extends JpaRepository<ExamTopicEntity, String> {
    boolean existsByCode(String code);
    Optional<ExamTopicEntity> findByIdAndIsActiveIsTrue(String id);

    @Query(value = """
            FROM ExamTopicEntity AS e LEFT JOIN FETCH e.questions AS q LEFT JOIN FETCH q.choices WHERE e.id = :id AND e.isActive = TRUE
            """)
    Optional<ExamTopicEntity> findByIdAndJoinQuestionAndChoices(@Param("id") String id);
}
