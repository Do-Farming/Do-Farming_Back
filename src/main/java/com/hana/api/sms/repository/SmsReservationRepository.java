package com.hana.api.sms.repository;


import com.hana.api.sms.entity.SmsReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SmsReservationRepository extends JpaRepository<SmsReservation, Long> {
    List<SmsReservation> findAllBySendingDateBetween(LocalDate from, LocalDate to);
}
