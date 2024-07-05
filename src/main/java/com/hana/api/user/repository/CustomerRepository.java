package com.hana.api.user.repository;

import com.hana.api.user.dto.CustomerContactDto;
import com.hana.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT new com.hana.api.user.dto.CustomerContactDto(userCode, name, phoneNumber) " +
            "FROM user " )
    List<CustomerContactDto> findCustomerContact();

    Optional<User> findByUserCode(UUID userCode);
}
