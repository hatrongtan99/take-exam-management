package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepo extends JpaRepository<AuthorityEntity, String> {
    Optional<AuthorityEntity> findByName(String name);
}
