package com.example.toyou.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FcmResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getTokenDto {
        private String token;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMyNameDto {

        @Schema(description = "본인 닉네임(FCM 전송시 사용)", nullable = false, example = "짱구")
        private String myName;
    }
}
