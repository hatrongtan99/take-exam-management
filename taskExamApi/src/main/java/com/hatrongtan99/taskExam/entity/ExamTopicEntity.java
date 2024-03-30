package com.hatrongtan99.taskExam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "examTopic")
public class ExamTopicEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String code;

    private String title;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "examTopic_question",
            joinColumns = @JoinColumn(name = "examTopicId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "questionId", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<ExamQuestionEntity> questions = new HashSet<>();

    private Integer questionNumber;

    @Temporal(TemporalType.TIME)
    private Duration timeExpires;

    @Builder.Default
    private Boolean isActive = true;

    public void addQuestion(ExamQuestionEntity question) {
        this.questions.add(question);
    }

    public void removeQuestion(ExamQuestionEntity question) {
        this.questions.remove(question);
    }
    public void removeQuestion(Collection<ExamQuestionEntity> question) {
        this.questions.removeAll(question);
    }
}
