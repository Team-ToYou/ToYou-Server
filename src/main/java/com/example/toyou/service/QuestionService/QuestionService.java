package com.example.toyou.service.QuestionService;

import com.example.toyou.app.dto.QuestionRequest;

public interface QuestionService {

    void createQuestion(Long userId, QuestionRequest.createQuestionDTO request);
}
