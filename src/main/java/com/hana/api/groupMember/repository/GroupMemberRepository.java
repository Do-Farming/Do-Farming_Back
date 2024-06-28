package com.hana.api.groupMember.repository;

import com.hana.api.groupMember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("SELECT COUNT(gm) FROM group_member gm WHERE gm.group.id = :groupId")
    int countByGroupId(Long groupId);
}
