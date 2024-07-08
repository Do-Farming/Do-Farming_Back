package com.hana.api.quiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "question")
@Table(name = "question")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private long id;

    @Column(nullable = false)
    private String question;
}
