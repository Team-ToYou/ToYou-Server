package com.example.toyou.service.QuestionService;

import com.example.toyou.app.dto.QuestionRequest;

public interface QuestionService {

    void createOptional(Long userId, QuestionRequest.createOptionalDTO request);
}
