package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetQuestionsDTO {
        private List<QuestionResponse.QuestionInfo> questionList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionInfo {
        private Long questionId;
        private String content;
        private QuestionType questionType;
        private String questioner;
        private List<String> answerOption;
    }
}
