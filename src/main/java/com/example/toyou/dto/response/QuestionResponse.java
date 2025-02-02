package com.example.toyou.dto.response;

import com.example.toyou.domain.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "질문 식별자", nullable = false, example = "1")
        private Long questionId;
        @Schema(description = "질문 내용", nullable = false, example = "어제 뭐했어?")
        private String content;
        @Schema(description = "질문 유형", nullable = false, example = "LONG_ANSWER")
        private QuestionType questionType;
        @Schema(description = "질문자", nullable = false, example = "짱구")
        private String questioner;
        @Schema(description = "답변 선택란", nullable = true, example = "null")
        private List<String> answerOption;
    }
}
