package com.hana.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hana.api.challenge.entity.wakeupChallenge;
import com.hana.api.group.entity.Group;
import com.hana.api.groupMember.entity.GroupMember;
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
    private String status;

    @Column(columnDefinition = "BINARY(16)", name = "device_id")
    private UUID deviceId;

    private String userImg;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<wakeupChallenge> wakeupChallenge;
}
