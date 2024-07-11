package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.QuestionType;
import lombok.Getter;

import java.util.List;

public class CardRequest {

    @Getter
    public static class createCardDTO {
        private boolean exposure;
        private List<CardRequest.qa> questionList;
    }

    @Getter
    public static class updateCardDTO {
        private boolean exposure;
        private List<CardRequest.qa> questionList;
    }

    @Getter
    public static class qa {
        private Long questionId;
        private String answer;
    }
}
