package com.example.toyou.repository;

import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByUserAndCreatedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Question> findByCreatedAtBeforeAndDiaryCardIsNull(LocalDateTime startOfDay);
}
