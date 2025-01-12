package com.example.counselserver.repository;

import com.example.counselserver.entity.Counsel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
    Optional<Counsel> findByEmail(String email);
}
