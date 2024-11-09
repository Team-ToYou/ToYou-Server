package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CardRequest {

    @Getter
    public static class createCardDTO {

        @Schema(description = "공개 여부", nullable = false, example = "true")
        private boolean exposure;
        private List<CardRequest.qa> questionList;
    }

    @Getter
    @Builder
    public static class updateCardDTO {
        @Schema(description = "공개 여부", nullable = false, example = "true")
        private boolean exposure;
        private List<CardRequest.qa> questionList;
    }

    @Getter
    @Builder
    public static class qa {

        @Schema(description = "질문 식별자", nullable = false, example = "1")
        private Long questionId;
        @Schema(description = "답변", nullable = false, example = "짜장면")
        private String answer;
    }
}
