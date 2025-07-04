package com.example.toyou.repository;

import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<DiaryCard, Long> {
    List<DiaryCard> findByUser(User user);

    @Query("""
        SELECT c.id FROM DiaryCard c
        WHERE c.user = :user
        AND c.createdAt BETWEEN :start AND :end
    """)
    Optional<Long> findTodayCardId(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
