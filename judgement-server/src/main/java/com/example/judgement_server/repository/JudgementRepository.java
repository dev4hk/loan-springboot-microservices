package com.example.judgement_server.repository;

import com.example.judgement_server.entity.Judgement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    Optional<Judgement> findByApplicationId(Long applicationId);
}
