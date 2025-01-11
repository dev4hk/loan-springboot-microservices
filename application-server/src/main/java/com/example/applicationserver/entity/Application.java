package com.example.applicationserver.entity;

import com.example.applicationserver.constants.CommunicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLRestriction("is_deleted=false")
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    private String firstname;

    private String lastname;

    private String cellPhone;

    private String email;

    private BigDecimal interestRate;

    private BigDecimal fee;

    private LocalDateTime maturity;

    private BigDecimal hopeAmount;

    private LocalDateTime appliedAt;

    private BigDecimal approvalAmount;

    private LocalDateTime contractedAt;

    @Enumerated(EnumType.STRING)
    private CommunicationStatus communicationStatus;

}