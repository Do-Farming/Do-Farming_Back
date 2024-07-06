package com.hana.api.taste.entity;

import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "taste")
@Table(name = "taste")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class Taste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taste_id")
    private long id;

    @Column(nullable = false)
    private String tasteTitle;

    @Column(nullable = false)
    private String tasteCategory;

    @Column(nullable = false)
    private String tasteTag;

    @Column(nullable = false)
    private String tasteImg;
}
