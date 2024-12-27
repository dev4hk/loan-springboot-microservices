package com.example.termsserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted=false")
public class Terms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long termsId;

    @Column(columnDefinition = "varchar(255) NOT NULL COMMENT 'Terms'")
    private String name;

    @Column(columnDefinition = "varchar(255) NOT NULL COMMENT 'Terms detail URL'")
    private String termsDetailUrl;
}