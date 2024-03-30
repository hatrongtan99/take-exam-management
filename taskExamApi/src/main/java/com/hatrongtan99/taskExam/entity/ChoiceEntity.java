package com.hatrongtan99.taskExam.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "choice")
public class ChoiceEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId")
    private ExamQuestionEntity questionId;

    private String title;

    @Builder.Default
    private Boolean isAnswer = false;

    @Builder.Default
    private Boolean isActive = true;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ChoiceEntity)) {
            return false;
        }
        return id != null && id.equals(((ChoiceEntity) o).id);
    }
}
