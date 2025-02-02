package com.example.toyou.dto.response;

import com.example.toyou.domain.enums.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HomeResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHomeDTO {
        @Schema(description = "닉네임", nullable = false, example = "짱구")
        private String nickname;
        @Schema(description = "감정", nullable = false, example = "HAPPY")
        private Emotion emotion;
        @Schema(description = "질문 개수", nullable = false, example = "5")
        private Integer questionNum;
        @Schema(description = "카드 식별자", nullable = false, example = "1")
        private Long cardId;
        @Schema(description = "알림 확인 여부", nullable = false, example = "false")
        private boolean uncheckedAlarm;
    }
}
