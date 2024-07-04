package com.hana.api.group.repository;

import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByIsPublicTrue();

    @Query("SELECT gm.user FROM group_member gm WHERE gm.group.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT g.id FROM group g")
    List<Long> findAllGroupIds();

    @Query(value = "SELECT * from group_table where ?1 between group_table.started_at and group_table.ended_at", nativeQuery = true)
    List<Group> findActiveGroup(LocalDate tomorrow);
}
