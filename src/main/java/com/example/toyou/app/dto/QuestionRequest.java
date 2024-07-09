package com.example.toyou.app.dto;

import lombok.Getter;

import java.util.List;

public class QuestionRequest {

    @Getter
    public static class createOptionalDTO{
        private String target;
        private String content;
        private List<String> answerOptionList;
        private boolean anonymous;
    }
}
