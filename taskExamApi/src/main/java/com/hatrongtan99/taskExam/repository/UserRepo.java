package com.hatrongtan99.taskExam.repository;

import com.hatrongtan99.taskExam.entity.UserEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, String> {

    @Query(value = """
            FROM UserEntity AS u JOIN FETCH u.authorities WHERE u.email = :email AND u.isActive = TRUE
            """)
    Optional<UserEntity> findByEmailAndIsActiveIsTrue(@Param("email") String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByIdAndIsActiveIsTrue(String userId);

    @Query(value = """
                FROM UserEntity AS u JOIN u.authorities AS a where (:roleName IS NULL OR a.name = :roleName)
            """)
    Page<UserEntity> findAll(@Param("roleName") String roleName, PageRequest pageRequest);
}
