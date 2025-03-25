package com.example.toyou.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class appleLoginDTO {
        @Schema(description = "회원 여부", nullable = false, example = "true")
        private boolean isUser;
        @Schema(description = "access 토큰", nullable = false, example = "...")
        private String accessToken;
        @Schema(description = "refresh 토큰", nullable = false, example = "...")
        private String refreshToken;
    }
}
