package com.example.toyou.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class checkUserNicknameDTO {

        @Schema(description = "닉네임 중복 여부", nullable = false, example = "true")
        private boolean exists;
    }
}
