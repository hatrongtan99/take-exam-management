package com.hatrongtan99.taskExam.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "authority")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Builder.Default
    private Boolean isActive = true;
}
