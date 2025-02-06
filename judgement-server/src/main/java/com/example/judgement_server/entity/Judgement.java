package com.example.judgement_server.entity;

import com.example.judgement_server.constants.CommunicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Long judgementId;

    private Long applicationId;

    private String firstname;

    private String lastname;

    private BigDecimal approvalAmount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer payDay;

    private BigDecimal monthlyPayment;

    private Integer numberOfPayments;

    private BigDecimal interest;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private CommunicationStatus communicationStatus;

}
