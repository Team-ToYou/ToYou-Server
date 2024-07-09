package com.example.toyou.repository;

import com.example.toyou.domain.DiaryCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<DiaryCard, Long> {
}
