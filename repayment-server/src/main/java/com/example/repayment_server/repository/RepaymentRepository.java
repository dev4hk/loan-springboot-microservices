package com.example.repayment_server.repository;

import com.example.repayment_server.entity.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {
    List<Repayment> findAllByApplicationId(Long applicationId);
}
