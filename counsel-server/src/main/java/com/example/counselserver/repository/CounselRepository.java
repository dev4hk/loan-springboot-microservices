package com.example.counselserver.repository;

import com.example.counselserver.entity.Counsel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
}
