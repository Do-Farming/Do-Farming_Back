package com.hana.api.groupMember.repository;

import com.hana.api.groupMember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("SELECT COUNT(gm) FROM group_member gm WHERE gm.group.id = :groupId")
    int countByGroupId(Long groupId);

    // 사용자가 그룹에 가입되어 있는지 확인
    @Query("SELECT gm FROM group_member gm WHERE gm.user.userCode = :userCode")
    List<GroupMember> findByUserUserCode(@Param("userCode") UUID userCode);

    // 사용자가 특정 그룹에 가입되어 있는지 확인
    @Query("SELECT COUNT(gm) > 0 FROM group_member gm WHERE gm.group.id = :groupId AND gm.user.userCode = :userCode")
    boolean isAlreadyJoin(@Param("groupId") Long groupId, @Param("userCode") UUID userCode);

    @Query("SELECT DISTINCT gm.group.id FROM group_member gm WHERE gm.user.phoneNumber = :userId")
    Long findGroupByUserId(@Param("userId") String userId);
}
