package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.QuestionType;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class QuestionRequest {

    @Getter
    @Builder
    public static class createQuestionDTO{
        private String target;
        private String content;
        private QuestionType questionType;
        private boolean anonymous;
        private List<String> answerOptionList;
    }
}
