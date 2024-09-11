package com.example.toyou.repository;

import com.example.toyou.domain.mappings.UserCustomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCustomQuestionRepository extends JpaRepository<UserCustomQuestion, Long> {

    List<UserCustomQuestion> findByUserId(Long userId);
}
