package com.example.repayment_server.entity;

import com.example.repayment_server.constants.CommunicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted=false")
public class Repayment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repaymentId;

    private Long applicationId;

    private BigDecimal repaymentAmount;

    @Enumerated(EnumType.STRING)
    private CommunicationStatus communicationStatus;
}
