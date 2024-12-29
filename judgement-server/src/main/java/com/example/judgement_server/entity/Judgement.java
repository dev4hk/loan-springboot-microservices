package com.example.judgement_server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted=false")
public class Judgement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long judgmentId;

    private Long applicationId;

    @Column(columnDefinition = "varchar(12) NOT NULL COMMENT 'Judge'")
    private String name;

    private BigDecimal approvalAmount;


}
