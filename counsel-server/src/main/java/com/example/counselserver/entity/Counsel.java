package com.example.counselserver.entity;

import com.example.counselserver.constants.CommunicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLRestriction("is_deleted=false")
public class Counsel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselId;

    private LocalDateTime appliedAt;

    private String firstname;

    private String lastname;

    private String cellPhone;

    private String email;

    private String memo;

    private String address1;

    private String address2;

    private String city;

    private String state;

    private String zipCode;

    @Enumerated(EnumType.STRING)
    private CommunicationStatus communicationStatus;
}
