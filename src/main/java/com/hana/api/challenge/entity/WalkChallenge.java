package com.hana.api.challenge.entity;

import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "walk_challenge")
@Table(name = "walk_challenge")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class WalkChallenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walk_id")
    private Long id;

    @Column(nullable = false)
    private int step;

    @Column(nullable = false)
    private LocalDate walkDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private User user;
}
