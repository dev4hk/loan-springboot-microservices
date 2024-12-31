package com.example.balance_server.repository;

import com.example.balance_server.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByApplicationId(Long applicationId);
}
