package com.example.judgement_server.repository;

import com.example.judgement_server.dto.CommunicationStatusStats;
import com.example.judgement_server.entity.Judgement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    Optional<Judgement> findByApplicationId(Long applicationId);

    @Query("SELECT j.communicationStatus AS communicationStatus, COUNT(j) AS count " +
            "FROM Judgement j GROUP BY j.communicationStatus")
    List<CommunicationStatusStats> getCommunicationStatusStats();
}
