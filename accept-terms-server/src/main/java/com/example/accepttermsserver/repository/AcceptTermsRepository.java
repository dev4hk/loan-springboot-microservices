package com.example.accepttermsserver.repository;

import com.example.accepttermsserver.entity.AcceptTerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcceptTermsRepository extends JpaRepository<AcceptTerms, Long> {

    Boolean existsByApplicationIdAndTermsId(long applicationId, long termsId);

}
