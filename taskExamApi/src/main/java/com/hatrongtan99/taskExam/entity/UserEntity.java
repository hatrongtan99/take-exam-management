package com.hatrongtan99.taskExam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_authority")
    private AuthorityEntity authorities;

    private String fullname;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private Boolean isActive = true;
}
