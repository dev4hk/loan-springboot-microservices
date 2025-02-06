package com.example.entry_server.repository;

import com.example.entry_server.dto.CommunicationStatusStats;
import com.example.entry_server.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    Optional<Entry> findByApplicationId(Long applicationId);

    @Query("SELECT e.communicationStatus AS communicationStatus, COUNT(e) AS count " +
            "FROM Entry e GROUP BY e.communicationStatus")
    List<CommunicationStatusStats> getCommunicationStatusStats();
}
