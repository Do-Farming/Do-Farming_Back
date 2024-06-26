package com.hana.api.challenge.entity;

import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "challenge_record")
@Table(name = "challenge_record")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class challengeRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_record_id")
    long id;

    @Column(nullable = false)
    private String challengeDate;

    @Column(nullable = false)
    private LocalDateTime challengeType;
}
