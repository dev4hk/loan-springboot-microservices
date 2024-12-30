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

    private String firstname;

    private String lastname;

    private BigDecimal approvalAmount;


}
