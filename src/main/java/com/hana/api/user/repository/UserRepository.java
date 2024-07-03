package com.hana.api.user.repository;


import com.hana.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // socialNumber로 User 찾기
    Optional<User> findByIdentificationNumber(String IdentificationNumber);
    Optional<User> findByName(String name);
    Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<User> findByUserCode(UUID userCode);

    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);
}
