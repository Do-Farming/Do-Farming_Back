package com.hana.api.groupMember.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "group_member")
@Table(name = "group_member")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class GroupMember extends BaseEntity {
    @EmbeddedId
    private GroupMemberPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userCode")
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private User user;
}
