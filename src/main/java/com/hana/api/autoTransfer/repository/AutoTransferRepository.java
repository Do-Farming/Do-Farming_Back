package com.hana.api.autoTransfer.repository;

import com.hana.api.autoTransfer.entity.AutoTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AutoTransferRepository extends JpaRepository<AutoTransfer, Long> {
    List<AutoTransfer> findByAutoTransferDay(int day);
}