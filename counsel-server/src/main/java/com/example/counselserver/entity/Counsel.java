package com.example.counselserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String address;

    private String addressDetail;

    private String zipCode;
}
