package com.hana.api.user.entity;

import com.hana.api.account.entity.Account;
import com.hana.api.challenge.entity.QuizChallenge;
import com.hana.api.challenge.wakeup.entity.WakeupChallenge;
import com.hana.api.groupMember.entity.GroupMember;
import com.hana.api.weeklyRate.entity.WeeklyRate;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "user")
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@Setter
public class User extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)", name = "user_code")
    private UUID userCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String identificationNumber;

    @Column(nullable = false)
    private int status;

    @Column(unique = true, nullable = true)
    private String deviceId;

    @Column(nullable = false)
    private Integer userImg;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<WakeupChallenge> wakeupChallenge;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<QuizChallenge> quizChallenges;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<WeeklyRate> weeklyRates;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Account> accounts;
}
