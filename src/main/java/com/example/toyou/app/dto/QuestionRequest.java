package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class QuestionRequest {

    @Getter
    @Builder
    public static class createQuestionDTO{
        @Schema(description = "유저(질문 대상) ID", nullable = false, example = "1")
        private Long targetId;

        @Schema(description = "질문 내용", nullable = false, example = "어제 뭐했어?")
        private String content;

        @Schema(description = "질문 유형", nullable = false, example = "LONG_ANSWER")
        private QuestionType questionType;

        @Schema(description = "익명 여부", nullable = false, example = "true")
        private boolean anonymous;

        @Schema(description = "답변 선택란", nullable = true, example = "null")
        private List<String> answerOptionList;
    }
}
