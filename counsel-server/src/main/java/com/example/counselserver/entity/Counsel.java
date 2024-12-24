package com.example.counselserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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

    private String name;

    private String cellPhone;

    private String email;

    private String memo;

    private String address;

    private String addressDetail;

    private String zipCode;
}
