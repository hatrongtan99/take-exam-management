package com.hatrongtan99.taskExam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "studentChoice")
public class StudentAnswerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "takeExamId")
    private TakeExamEntity takeExamId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "questionId")
    private ExamQuestionEntity questionId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(name = "student_choice_answer",
            joinColumns = @JoinColumn(name = "studentChoiceId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "choiceId", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<ChoiceEntity> choices = new HashSet<>();

    @Builder.Default
    private Boolean isActive = true;
}
