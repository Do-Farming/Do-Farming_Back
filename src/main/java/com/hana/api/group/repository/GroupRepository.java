package com.hana.api.group.repository;

import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByIsPublicTrue();

    @Query("SELECT gm.user FROM group_member gm WHERE gm.group.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") Long groupId);

    @Query(value = "SELECT group_table.id from group_table where ?1 between group_table.started_at and group_table.ended_at", nativeQuery = true)
    List<Long> findAllGroupIds(LocalDate now);

    @Query(value = "SELECT * from group_table where ?1 between group_table.started_at and group_table.ended_at", nativeQuery = true)
    List<Group> findActiveGroup(LocalDate tomorrow);

    @Query(value = "SELECT g FROM group_table g WHERE g.group_id IN (SELECT gm.group_id FROM group_member gm WHERE gm.user_code = :userCode)", nativeQuery = true)
    Optional<Group> findGroupByUserCode(@Param("userCode") UUID userCode);
}
