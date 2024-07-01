package com.hana.api.group.repository;

import com.hana.api.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByIsPublicTrue();

}
