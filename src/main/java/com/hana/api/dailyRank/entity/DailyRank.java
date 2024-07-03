package com.hana.api.dailyRank.entity;

import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Entity(name = "daily_rank")
@Table(name = "daily_rank")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@Setter
public class DailyRank extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_rank_id")
    long id;

    @Column(nullable = false)
    private LocalDateTime dailyDate;

    @Column(nullable = false)
    private int dailyRank;

    @Column(nullable = false)
    private double dailyRate;

    @Column(nullable = false)
    private double totalRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private User user;
}
