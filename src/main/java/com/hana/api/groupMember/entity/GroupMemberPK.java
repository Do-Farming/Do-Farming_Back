package com.hana.api.groupMember.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor
public class GroupMemberPK implements Serializable {

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_code")
    private UUID userCode;


    public GroupMemberPK(Long groupId, UUID userCode) {
        this.groupId = groupId;
        this.userCode = userCode;
    }
}