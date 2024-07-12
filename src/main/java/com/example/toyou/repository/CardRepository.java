package com.example.toyou.repository;

import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<DiaryCard, Long> {
    List<DiaryCard> findByUser(User user);
}
