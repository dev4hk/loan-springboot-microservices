package com.example.applicationserver.repository;

import com.example.applicationserver.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByEmail(String email);
}
