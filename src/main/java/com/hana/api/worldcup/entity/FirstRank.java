package com.hana.api.worldcup.entity;

import com.hana.api.user.entity.User;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "first_rank")
@Table(name = "first_rank")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class FirstRank extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "first_rank_id")
    private long id;

    @Column(nullable = false)
    private String worldCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worldcup_id", referencedColumnName = "worldcup_id")
    private Worldcup worldcup;

}
