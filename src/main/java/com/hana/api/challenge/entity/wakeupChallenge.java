package com.hana.api.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "wakeup_challenge")
@Table(name = "wakeup_challenge")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class wakeupChallenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wakeup_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime wakeupDate;

    @Column(nullable = false)
    private LocalDateTime wakeupTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private User user;
}
