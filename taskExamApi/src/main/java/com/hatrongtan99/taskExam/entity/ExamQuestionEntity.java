package com.hatrongtan99.taskExam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "examQuestion")
public class ExamQuestionEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    @OneToMany(
            cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true,
            mappedBy = "questionId",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<ChoiceEntity> choices = new HashSet<>();

    @Builder.Default
    private Boolean isActive = true;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExamQuestionEntity)) {
            return false;
        }
        return id != null && id.equals(((ExamQuestionEntity) other).getId());
    }
}
