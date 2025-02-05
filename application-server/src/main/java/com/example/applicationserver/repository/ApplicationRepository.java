package com.example.applicationserver.repository;

import com.example.applicationserver.dto.CommunicationStatusStats;
import com.example.applicationserver.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByEmail(String email);

    @Query("SELECT a.communicationStatus AS communicationStatus, COUNT(a) AS count " +
            "FROM Application a GROUP BY a.communicationStatus")
    List<CommunicationStatusStats> getCommunicationStatusStats();
}
