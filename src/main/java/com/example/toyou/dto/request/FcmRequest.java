package com.example.toyou.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class FcmRequest {

    @Getter
    public static class saveTokenDto {

        @Schema(description = "FCM 토큰", nullable = false, example = "...")
        private String token;
    }

    @Getter
    public static class updateTokenDto {

        @Schema(description = "FCM 토큰", nullable = false, example = "...")
        private String token;
    }

    @Getter
    public static class deleteTokenDto {

        @Schema(description = "FCM 토큰", nullable = false, example = "...")
        private String token;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class sendMessageDto {

        @Schema(description = "요청 대상의 FCM 토큰", nullable = false, example = "...")
        private String token;

        @Schema(description = "제목", nullable = false, example = "친구 요청")
        private String title;

        @Schema(description = "내용", nullable = false, example = "짱구님이 친구 요청을 보냈습니다.")
        private String body;
    }
}
