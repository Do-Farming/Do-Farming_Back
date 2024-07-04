package com.hana.api.group.repository;

import com.hana.api.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByIsPublicTrue();

    @Query(value = "SELECT * from group where group.started_at <= ?1 and group.ended_at > ?1", nativeQuery = true)
    List<Group> findActiveGroup(LocalDate tomorrow);
}
