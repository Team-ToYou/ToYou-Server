package com.example.toyou.repository;

import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByUserAndCreatedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Question> findByCreatedAtBeforeAndDiaryCardIsNull(LocalDateTime startOfDay);

    @Query("SELECT COUNT(q) FROM Question q WHERE q.user = :user AND DATE(q.createdAt) = CURRENT_DATE")
    int countTodayQuestions(@Param("user") User user);
}
