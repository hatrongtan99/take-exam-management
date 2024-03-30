package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExamQuestionRepo extends JpaRepository<ExamQuestionEntity, String> {
    Optional<ExamQuestionEntity> findByIdAndIsActiveIsTrue(String id);


    @Query(value = """
            FROM ExamQuestionEntity AS e LEFT JOIN FETCH e.choices WHERE e.id = :id AND e.isActive = TRUE
            """)
    Optional<ExamQuestionEntity> findByIdAndJoinChoices(@Param("id") String id);
}
