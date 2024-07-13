package com.example.toyou.repository;

import com.example.toyou.domain.CustomQuestion;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomQuestionRepository extends JpaRepository<CustomQuestion, Long> {

    List<CustomQuestion> findByUserStatus(Status userStatus);
}

