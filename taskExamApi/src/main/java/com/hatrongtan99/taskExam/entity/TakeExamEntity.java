package com.hatrongtan99.taskExam.entity;

import com.hatrongtan99.taskExam.entity.enumeration.TakeExamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "takeExam")
public class TakeExamEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId")
    private UserEntity studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examTopicId")
    private ExamTopicEntity examTopicId;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST},
            mappedBy = "takeExamId"
    )
    @Builder.Default
    private Set<StudentAnswerEntity> answer = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private TakeExamStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime timeStart;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime timeSubmit;

    private Duration timeExpires;

    private Float score;
    private int questionNumber;

    @Builder.Default
    private Boolean isActive = true;
}
