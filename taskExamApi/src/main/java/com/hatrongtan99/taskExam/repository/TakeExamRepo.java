package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.ExamTopicEntity;
import com.hatrongtan99.taskExam.entity.TakeExamEntity;
import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.entity.enumeration.TakeExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TakeExamRepo extends JpaRepository<TakeExamEntity, String>, JpaSpecificationExecutor<TakeExamEntity> {

    @Query(value = """
        SELECT e.id FROM TakeExamEntity AS t JOIN t.examTopicId AS e WHERE t.id = ?1
        """)
    Optional<String> findByIdAndJoinTopic(String id);
    Optional<TakeExamEntity> findByStudentIdAndExamTopicIdAndStatus(UserEntity studentId, ExamTopicEntity examTopicId, TakeExamStatus status);

    @Query(value = """
        FROM TakeExamEntity AS t LEFT JOIN FETCH t.studentId AS s JOIN t.examTopicId AS e WHERE e.id = :topicId
        """)
    Page<TakeExamEntity> findByExamTopicId(@Param("topicId") String topic, Pageable pageable);

    @Query(value = """
        FROM TakeExamEntity AS t JOIN FETCH t.studentId AS s WHERE s.id = :studentId
        """)
    Page<TakeExamEntity> findByStudentId(@Param("studentId") String studentId, Pageable pageable);
}
