package com.example.toyou.repository;

import com.example.toyou.domain.CustomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomQuestionRepository extends JpaRepository<CustomQuestion, Long> {
}
