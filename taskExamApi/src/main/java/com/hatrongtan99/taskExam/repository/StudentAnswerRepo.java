package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.StudentAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnswerRepo extends JpaRepository<StudentAnswerEntity, String> {
}
