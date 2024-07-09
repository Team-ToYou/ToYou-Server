package com.example.toyou.service.QuestionService;

import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.app.dto.QuestionResponse;

public interface QuestionService {

    void createQuestion(Long userId, QuestionRequest.createQuestionDTO request);

    QuestionResponse.GetQuestionsDTO getQuestions(Long userId);
}
