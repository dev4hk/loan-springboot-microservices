package com.example.counselserver.repository;

import com.example.counselserver.constants.CommunicationStatus;
import com.example.counselserver.dto.CommunicationStatusStats;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.entity.Counsel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
    Optional<Counsel> findByEmail(String email);

    @Query("SELECT c.communicationStatus AS communicationStatus, COUNT(c) AS count " +
            "FROM Counsel c GROUP BY c.communicationStatus")
    List<CommunicationStatusStats> getCommunicationStatusStats();

    @Query("SELECT c FROM Counsel c WHERE c.communicationStatus = :status ORDER BY c.createdAt DESC")
    List<CounselResponseDto> getNewCounsels(@Param("status") CommunicationStatus status, Pageable pageable);
}
