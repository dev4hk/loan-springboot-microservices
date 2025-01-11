package com.example.entry_server.entity;

import com.example.entry_server.constants.CommunicationStatus;
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
public class Entry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long entryId;

    private Long applicationId;

    private BigDecimal entryAmount;

    @Enumerated(EnumType.STRING)
    private CommunicationStatus communicationStatus;
}
