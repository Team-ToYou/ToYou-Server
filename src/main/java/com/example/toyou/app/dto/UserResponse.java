package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Status;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyPageDTO {

        @Schema(description = "닉네임", nullable = false, example = "짱구")
        private String nickname;

        @Schema(description = "친구 수", nullable = false, example = "3")
        private int friendNum;

        @Schema(description = "현재 상태", nullable = false, example = "COLLEGE")
        private Status status;
    }
}
