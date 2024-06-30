package com.hana.api.groupMember.repository;

import com.hana.api.groupMember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("SELECT COUNT(gm) FROM group_member gm WHERE gm.group.id = :groupId")
    int countByGroupId(Long groupId);

    @Query("SELECT COUNT(gm) > 0 FROM group_member gm WHERE gm.group.id = :groupId AND gm.user.userCode = :userCode")
    boolean isAlreadyJoin(@Param("groupId") Long groupId, @Param("userCode") UUID userCode);

}
