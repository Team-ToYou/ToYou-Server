package com.example.toyou.repository;

import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByUserAndCreatedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Question> findByCreatedAtBeforeAndDiaryCardIsNull(LocalDateTime startOfDay);

    @Query("""
        SELECT COUNT(q) FROM Question q
        WHERE q.user = :user
        AND q.createdAt BETWEEN :start AND :end
    """)
    int countTodayQuestions(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
