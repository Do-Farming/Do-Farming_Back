package com.hana.api.challenge.entity;

import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "challenge_record")
@Table(name = "challenge_record")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class ChallengeRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_record_id")
    long id;

    @Column(nullable = false)
    private LocalDate challengeDate;

    @Column(nullable = false)
    private int challengeType;
}
