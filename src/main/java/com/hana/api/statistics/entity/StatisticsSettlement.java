package com.hana.api.statistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "statistics_settlement")
@Table(name = "statistics_settlement")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@Setter
public class StatisticsSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long depositAmount = 0L;

    @Column(nullable = false)
    private Long withdrawalAmount= 0L;

    @Column(nullable = false)
    private Long depositCount= 0L;

    @Column(nullable = false)
    private Long withdrawalCount= 0L;
}
