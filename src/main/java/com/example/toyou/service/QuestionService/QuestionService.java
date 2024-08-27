package com.example.toyou.service.QuestionService;

import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.app.dto.QuestionResponse;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.Emotion;

import java.io.IOException;

public interface QuestionService {

    void createQuestion(Long userId, QuestionRequest.createQuestionDTO request) ;

    QuestionResponse.GetQuestionsDTO getQuestions(Long userId);

    void deleteOldQuestions();

    void autoCreateQuestion(QuestionRequest.createQuestionDTO request);

    void deleteToYouQuestions(User user);
}
